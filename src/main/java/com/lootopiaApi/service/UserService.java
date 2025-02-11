package com.lootopiaApi.service;

import com.lootopiaApi.exception.InvalidTokenException;
import com.lootopiaApi.exception.UserAlreadyExistException;
import com.lootopiaApi.model.MfaTokenData;
import com.lootopiaApi.model.User;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.mail.MessagingException;

public interface UserService {
    MfaTokenData registerUser(User user) throws UserAlreadyExistException, QrGenerationException;
    //MfaTokenData mfaSetup(String email) throws UnkownIdentifierException, QrGenerationException;
    boolean verifyTotp(final String code,String username);

    void sendRegistrationConfirmationEmail(final User user) throws MessagingException;
    boolean verifyUser(final String token) throws InvalidTokenException;
}
