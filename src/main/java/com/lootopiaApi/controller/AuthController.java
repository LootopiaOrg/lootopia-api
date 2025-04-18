package com.lootopiaApi.controller;

import com.lootopiaApi.exception.InvalidTokenException;
import com.lootopiaApi.exception.UserAlreadyExistException;
import com.lootopiaApi.model.*;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.service.JWTService;
import com.lootopiaApi.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin
@RestController
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationProvider authenticationProvider;

    public AuthController(UserService userService, JWTService jwtService, AuthenticationProvider authenticationProvider) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.registerUser(user));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body("Username already exists.");
        } catch (QrGenerationException e) {
            return ResponseEntity.internalServerError().body("Failed to generate QR code.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during registration.");
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        // Validate the user credentials and return the JWT / send redirect to MFA page
        try {//Get the user and Compare the password
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            if (user.isMfaEnabled()){
                return ResponseEntity.ok(MfaVerificationResponse.builder()
                        .username(loginRequest.getUsername())
                        .tokenValid(Boolean.TRUE)
                        .authValid(Boolean.TRUE)
                        .mfaRequired(Boolean.TRUE)
                        .message("User Authenticated using username and Password")
                        .jwt("")
                        .build());
            } else {
                return ResponseEntity.ok(MfaVerificationResponse.builder()
                        .username(user.getUsername())
                        .authValid(true)
                        .tokenValid(true)
                        .mfaRequired(false)
                        .message("Authentication successful.")
                        .jwt(jwtService.generateJwt(user.getUsername()))
                        .build());
            }
        } catch (Exception e){
            return ResponseEntity.ok(MfaVerificationResponse.builder()
                    .username(loginRequest.getUsername())
                    .tokenValid(Boolean.FALSE)
                    .authValid(Boolean.FALSE)
                    .mfaRequired(Boolean.FALSE)
                    .message("Invalid Credentials. Please try again.")
                    .jwt("")
                    .build());
        }
    }

    @PostMapping("/verifyTotp")
    public ResponseEntity<?> verifyTotp(@Validated @RequestBody MfaVerificationRequest request) throws ParseException {

        MfaVerificationResponse mfaVerificationResponse = MfaVerificationResponse.builder()
                .username(request.getUsername())
                .tokenValid(Boolean.FALSE)
                .message("Token is not Valid. Please try again.")
                .build();
        // Vérifier le TOTP
        if(userService.verifyTotp(request.getTotp(), request.getUsername())){
            // Générer un JWT
            mfaVerificationResponse = MfaVerificationResponse.builder()
                    .username(request.getUsername())
                    .tokenValid(Boolean.TRUE)
                    .message("Token is valid")
                    .jwt(jwtService.generateJwt(request.getUsername()))
                    .build();
        }
        return ResponseEntity.ok(mfaVerificationResponse);
    }


    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) throws InvalidTokenException {
        try{
            if(userService.verifyUser(token)){
                return ResponseEntity.ok("Your email has been successfully verified.");
            } else {
                return ResponseEntity.ok("User details not found. Please login and regenerate the confirmation link.");
            }
        } catch (InvalidTokenException e){
            return ResponseEntity.ok("Link expired or token already verified.");
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        try {
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok(
                    new ApiResponse("Password reset link has been sent to your email.", "success")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Error: " + e.getMessage(), "error")
            );
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
        try {
            boolean success = userService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
            if (success) {
                return ResponseEntity.ok(
                        new ApiResponse("Password has been reset successfully.", "success")
                );
            } else {
                return ResponseEntity.badRequest().body(
                        new ApiResponse("Invalid or expired token.", "error")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Error: " + e.getMessage(), "error")
            );
        }
    }

}
