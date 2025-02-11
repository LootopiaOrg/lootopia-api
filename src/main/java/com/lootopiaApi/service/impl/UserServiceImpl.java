package com.lootopiaApi.service.impl;

import com.lootopiaApi.exception.InvalidTokenException;
import com.lootopiaApi.exception.MFAServerAppException;
import com.lootopiaApi.exception.UserAlreadyExistException;
import com.lootopiaApi.model.*;
import com.lootopiaApi.repository.EmailConfirmationTokenRepository;
import com.lootopiaApi.repository.RoleRepository;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.EmailService;
import com.lootopiaApi.service.TotpManager;
import com.lootopiaApi.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import jakarta.mail.MessagingException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManager totpManager;

    private final EmailService emailService;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    private final TimeProvider timeProvider;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TotpManager totpManager, EmailService emailService, EmailConfirmationTokenRepository emailConfirmationTokenRepository, TimeProvider timeProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.totpManager = totpManager;
        this.emailService = emailService;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.timeProvider = timeProvider;
    }

    @Override
    public MfaTokenData registerUser(User user) throws UserAlreadyExistException, QrGenerationException {
        // Vérifiez d'abord si l'utilisateur existe déjà
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists");
        }

        // Ensuite, essayez d'enregistrer l'utilisateur
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setSecretKey(totpManager.generateSecretKey());

            if (user.getRoles().isEmpty()) {
                Role defaultRole = roleRepository.findByRole(ERole.USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                user.getRoles().add(defaultRole);
            }

            User savedUser = userRepository.save(user);
            this.sendRegistrationConfirmationEmail(user);
            String qrCode = totpManager.getQRCode(savedUser.getSecretKey());
            return MfaTokenData.builder()
                    .mfaCode(savedUser.getSecretKey())
                    .qrCode(qrCode)
                    .build();
        } catch (QrGenerationException e) {
            System.out.println("QR generation failed for user: " + user.getUsername()+ e);
            throw e; // Relancez QrGenerationException sans l'encapsuler
        } catch (Exception e) {
            System.out.println("Unexpected error during registration for user: "+ user.getUsername()+ e);
            throw new MFAServerAppException("Exception while registering the user", e);
        }
    }

    @Override
    public boolean verifyTotp(String code, String username) {
        User user = userRepository.findByUsername(username).get();
        System.out.println("Code reçu: " + code);
        System.out.println("Clé secrète: " + user.getSecretKey());
        boolean isValid = totpManager.verifyTotp(code, user.getSecretKey());
        System.out.println("Résultat de la vérification: " + isValid);
        return isValid;
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
}
