package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.EmailConfirmationToken;
import com.lootopiaApi.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;

    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException {
        //MIME - HTML message
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailConfirmationToken.getUser().getUsername());
        helper.setSubject("Confirm you E-Mail - MFA Application Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ emailConfirmationToken.getUser().getFirstName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "Please click on below link to confirm your account."
                        + "<br/> "  + generateConfirmationLink(emailConfirmationToken.getToken())+"" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>"
                , true);

        sender.send(message);
    }

    @Override
    public void sendResetEmail(String email, String firstName, String token) throws MessagingException {
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;
        String body = "Click here to reset your password: " + resetUrl;

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Password reset request - " + email);
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear " + firstName + ",</h2>"
                        + "<br/>Click here to reset your password: "
                        + "<br/> "  + resetUrl +"" +
                        "<br/> Regards,<br/>" +
                        "</body>" +
                        "</html>"
                , true);

        sender.send(message);
    }


    private String generateConfirmationLink(String token){
        return "<a href=http://localhost:8080/confirm-email?token="+token+">Confirm Email</a>";
    }
}
