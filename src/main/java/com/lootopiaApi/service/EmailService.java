package com.lootopiaApi.service;

import com.lootopiaApi.model.entity.EmailConfirmationToken;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException;

    void sendResetEmail(String email, String firstName, String token) throws MessagingException;
}
