package com.lootopiaApi.service;

import com.lootopiaApi.model.EmailConfirmationToken;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException;
}
