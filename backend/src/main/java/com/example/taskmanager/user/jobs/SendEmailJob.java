package com.example.taskmanager.user.jobs;

import com.example.taskmanager.email.EmailService;
import com.example.taskmanager.user.entities.PasswordResetToken;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.entities.VerificationToken;
import com.example.taskmanager.user.repositories.PasswordResetRepository;
import com.example.taskmanager.user.repositories.UserRepository;
import com.example.taskmanager.user.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailJob {
    private final VerificationTokenRepository verificationTokenRepository;
    private final SpringTemplateEngine templateEngine;
    private final EmailService emailService;
    private final PasswordResetRepository passwordResetRepository;
    @Value("${app.base-url}")
    private String baseUrl;

    @Async
    @Transactional
    public void sendWelcomeEmail(User user, VerificationToken verificationToken) {
        if (user.getIsEmailVerified() || verificationToken == null) return;

        if (verificationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("Verification token expired for user: {}", user.getEmail());
            return;
        }

        String verificationLink = baseUrl + "/api/auth/verify-email?token=" + verificationToken.getId().getCode();
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("name", user.getFirstName());
        thymeleafContext.setVariable("email", user.getEmail());
        thymeleafContext.setVariable("verificationLink", verificationLink);

        String htmlBody = templateEngine.process("welcome-email", thymeleafContext);
        try {
            emailService.sendHtmlMessage(user.getEmail(), "Welcome to our platform", htmlBody);
            log.info("Verification token sent to {}", user.getEmail());
        } catch (Exception e) {
            log.warn("Failed to sent email to {}: {}", user.getEmail(), e.getMessage());
            return;
        }
        verificationToken.setLastSentAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);
    }

    @Async
    @Transactional
    public void sendResetPasswordEmail(User user, PasswordResetToken passwordResetToken) {
        if (passwordResetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("password reset token expires for user: {}", user.getEmail());
            return;
        }

        String passwordResetLink = baseUrl + "/api/auth/reset-password?token=" + passwordResetToken.getToken();
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("name", user.getFirstName());
        thymeleafContext.setVariable("email", user.getEmail());
        thymeleafContext.setVariable("otp",  passwordResetToken.getToken());
        thymeleafContext.setVariable("passwordResetLink",  passwordResetLink);

        String htmlBody = templateEngine.process("reset-password", thymeleafContext);
        try {
            emailService.sendHtmlMessage(user.getEmail(), "Password reset requested", htmlBody);
            log.info("Password reset token sent to {}", user.getEmail());
        } catch (Exception e) {
            log.warn("Failed to sent password reset token email to {}: {}", user.getEmail(), e.getMessage());
            return;
        }
    }

}
