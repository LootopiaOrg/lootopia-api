package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.ApiResponse;
import com.lootopiaApi.DTOs.MfaTokenData;
import com.lootopiaApi.DTOs.UserRegisterRequest;
import com.lootopiaApi.DTOs.UserUpdateDTO;
import com.lootopiaApi.exception.InvalidTokenException;
import com.lootopiaApi.exception.MFAServerAppException;
import com.lootopiaApi.exception.UserAlreadyExistException;
import com.lootopiaApi.model.entity.EmailConfirmationToken;
import com.lootopiaApi.model.entity.Role;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.model.enums.ERole;
import com.lootopiaApi.repository.EmailConfirmationTokenRepository;
import com.lootopiaApi.repository.RoleRepository;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.EmailService;
import com.lootopiaApi.service.TotpManager;
import com.lootopiaApi.service.UserBalanceService;
import com.lootopiaApi.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.mail.MessagingException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManager totpManager;

    private final EmailService emailService;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    private final UserBalanceService userBalanceService;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TotpManager totpManager, EmailService emailService, EmailConfirmationTokenRepository emailConfirmationTokenRepository, UserBalanceService userBalanceService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.totpManager = totpManager;
        this.emailService = emailService;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.userBalanceService = userBalanceService;
    }

    @Override
    public MfaTokenData registerUser(UserRegisterRequest request)
            throws UserAlreadyExistException, QrGenerationException {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists");
        }

        try {
            // Map DTO to Entity
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setBio(request.getBio());
            user.setActive(true);
            user.setAccountVerified(false);
            user.setMfaEnabled(false);
            user.setSecretKey(totpManager.generateSecretKey());

            // Assign default roles
            Set<Role> roles = new HashSet<>();
            Role defaultRole = roleRepository.findByRole(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
            roles.add(defaultRole);

            if (request.isPartner()) {
                Role partnerRole = roleRepository.findByRole(ERole.PARTNER)
                        .orElseThrow(() -> new RuntimeException("Error: Role PARTNER is not found."));
                roles.add(partnerRole);
            }
            user.setRoles(roles);

            // Save user
            User savedUser = userRepository.save(user);

            // Create balance if not ADMIN
            if (!user.getRoles().stream().anyMatch(role -> role.getRole().equals(ERole.ADMIN))) {
                this.userBalanceService.createBalanceFor(savedUser.getId());
            }

            // Send confirmation email
            this.sendRegistrationConfirmationEmail(savedUser);

            // Generate QR Code
            String qrCode = totpManager.getQRCode(savedUser.getSecretKey());

            return MfaTokenData.builder()
                    .mfaCode(savedUser.getSecretKey())
                    .qrCode(qrCode)
                    .build();

        } catch (QrGenerationException e) {
            System.out.println("QR generation failed for user: " + request.getUsername() + e);
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error during registration for user: " + request.getUsername() + e);
            throw new MFAServerAppException("Exception while registering the user", e);
        }
    }

    @Override
    public boolean verifyTotp(String code, String username) {
        User user = userRepository.findByUsername(username).get();
        return totpManager.verifyTotp(code, user.getSecretKey());
    }

    @Override
    public void sendRegistrationConfirmationEmail(User user) throws MessagingException {
        // Generate the token
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setToken(tokenValue);
        emailConfirmationToken.setTimeStamp(LocalDateTime.now());
        emailConfirmationToken.setUser(user);
        emailConfirmationTokenRepository.save(emailConfirmationToken);
        // Send email
        emailService.sendConfirmationEmail(emailConfirmationToken);
    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenRepository.findByToken(token);
        if(Objects.isNull(emailConfirmationToken) || !token.equals(emailConfirmationToken.getToken())){
            throw new InvalidTokenException("Token is not valid");
        }
        User user = emailConfirmationToken.getUser();
        if (Objects.isNull(user)){
            return false;
        }
        user.setAccountVerified(true);
        userRepository.save(user);
        emailConfirmationTokenRepository.delete(emailConfirmationToken);
        return true;
    }

    public void initiatePasswordReset(String email) throws MessagingException {
        User user = userRepository.findByUsername(email).get();
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        emailService.sendResetEmail(user.getUsername(), user.getFirstName(), token);
    }

    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return (User) authentication.getPrincipal();
    }

    boolean isPartner() {
        return getAuthenticatedUser().getRoles().stream()
                .anyMatch(role -> role.getRole().name().equals("PARTNER"));
    }

    @Override
    public void assertIsPartner() throws AccessDeniedException {
        if (!isPartner()) {
            throw new AccessDeniedException("Access denied: Only partners can perform this action.");
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public ApiResponse toggleMfa(User user) {
        boolean current = user.isMfaEnabled();
        user.setMfaEnabled(!current);
        save(user);

        return new ApiResponse(
                "MFA " + (!current ? "enabled" : "disabled") + " successfully.",
                "success"
        );
    }

    public void updateUserProfile(User user, UserUpdateDTO request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBio(request.getBio());
        userRepository.save(user);
    }


}
