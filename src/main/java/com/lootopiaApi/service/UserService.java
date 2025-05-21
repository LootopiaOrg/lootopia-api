package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.UserUpdateDTO;
import com.lootopiaApi.exception.InvalidTokenException;
import com.lootopiaApi.exception.UserAlreadyExistException;
import com.lootopiaApi.DTOs.ApiResponse;
import com.lootopiaApi.DTOs.MfaTokenData;
import com.lootopiaApi.model.entity.User;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.mail.MessagingException;

import java.nio.file.AccessDeniedException;

public interface UserService {

    MfaTokenData registerUser(User user) throws UserAlreadyExistException, QrGenerationException;
    //MfaTokenData mfaSetup(String email) throws UnkownIdentifierException, QrGenerationException;
    boolean verifyTotp(final String code,String username);

    void sendRegistrationConfirmationEmail(final User user) throws MessagingException;
    boolean verifyUser(final String token) throws InvalidTokenException;

    void initiatePasswordReset(String email) throws MessagingException;
    boolean resetPassword(String token, String newPassword);

    User getAuthenticatedUser();
    void assertIsPartner() throws AccessDeniedException;

    public void save(User user);

    ApiResponse toggleMfa(User user);

    public void updateUserProfile(User user, UserUpdateDTO request);


}
