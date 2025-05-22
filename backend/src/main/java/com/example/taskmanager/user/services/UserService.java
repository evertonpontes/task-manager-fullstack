package com.example.taskmanager.user.services;

import com.example.taskmanager.config.auth.services.TokenService;
import com.example.taskmanager.user.dtos.*;
import com.example.taskmanager.user.entities.*;
import com.example.taskmanager.user.jobs.SendEmailJob;
import com.example.taskmanager.user.mapper.UserMapper;
import com.example.taskmanager.user.repositories.PasswordResetRepository;
import com.example.taskmanager.user.repositories.UserRepository;
import com.example.taskmanager.user.repositories.VerificationTokenRepository;
import com.example.taskmanager.utils.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final SendEmailJob sendEmailJob;
    private final TokenService tokenService;
    private final PasswordResetRepository passwordResetRepository;

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        String firstName = request.firstName();
        String lastName = request.lastName();
        String email = request.email().toLowerCase();
        String password = request.password();

        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser == null) { // if user not exists
            User newUser = userRepository.save(User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(UserRolesEnum.USER)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .isEmailVerified(false)
                    .failedLoginAttempts(0)
                    .build());

            sendVerificationCode(newUser);
            return userMapper.userToResponseData(newUser);

        } else { // if users exists
            if (existingUser.getIsEmailVerified()) throw new UsernameAlreadyExistsException("Email %s is already in use.". formatted(existingUser.getEmail())); // if user email is already verified

            Optional<VerificationToken> verificationTokenOpt = verificationTokenRepository
                    .findTopByIdIdentifierOrderByLastSentAtDesc(existingUser.getEmail()); // fetch if exists the last sent verification token

            if (verificationTokenOpt.isPresent()) {
                VerificationToken verificationToken = verificationTokenOpt.get();

                LocalDateTime lastSentAt = verificationToken.getLastSentAt();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime limit = lastSentAt.plusSeconds(30);

                if (now.isBefore(limit)) {
                    Duration secondsLeft = Duration.between(now, limit);
                    long seconds = secondsLeft.getSeconds();
                    throw new VerificationRateLimitException("Rate limit exceeded, please wait %d seconds to requesting a new verification token.".formatted(seconds));
                }
            }

            sendVerificationCode(existingUser);
            return userMapper.userToResponseData(existingUser);
        }
    }

    private void sendVerificationCode(User user) {
        VerificationToken verificationToken = VerificationToken.builder()
                .id(new VerificationTokenId(user.getEmail(), UUID.randomUUID().toString()))
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        verificationTokenRepository.deleteAllByIdIdentifier(user.getEmail());
        VerificationToken savedVerificationToken = verificationTokenRepository.save(verificationToken);
        sendEmailJob.sendWelcomeEmail(user, savedVerificationToken);
    }

    @Transactional
    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByIdCode(token).orElseThrow(() -> new TokenNotFoundException("Invalid verification token provided."));

        if (verificationToken.getExpiredAt().isBefore(LocalDateTime.now())) throw new TokenValidateException("Expired verification token.");

        User user = userRepository.findByEmail(verificationToken.getId().getIdentifier()).orElseThrow(() -> new UsernameNotFoundException("User with email %s not found."
                .formatted(verificationToken.getId().getIdentifier())));

        user.setIsEmailVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    public UserResponse findByAccessToken(String accessToken) {
        String email = tokenService.validateToken(accessToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email %s not found.".formatted(email)));
        return userMapper.userToResponseData(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.email().toLowerCase();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email %s not found.".formatted(email)));
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(PasswordResetToken.generateOtp())
                .user(user)
                .used(false)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetRepository.save(passwordResetToken);
        sendEmailJob.sendResetPasswordEmail(user, passwordResetToken);
    }

    public void resetPassword(String token, ResetPasswordRequest request) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Password reset token not found."));
        if (passwordResetToken.getUsed() || passwordResetToken.getExpiredAt().isBefore(LocalDateTime.now())) throw new TokenValidateException("Invalid password reset token provided.");

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.password()));

        passwordResetToken.setUsed(true);
        userRepository.save(user);
    }

    public UserResponse update(UpdateUserRequest request) {
        String firstName = request.firstName();
        String lastName = request.lastName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        authUser.setFirstName(firstName);
        authUser.setLastName(lastName);

        authUser = userRepository.save(authUser);
        return userMapper.userToResponseData(authUser);
    }

    public UserResponse updatePassword(UpdateUserPassword request) {
        String currentPassword = request.currentPassword();
        String newPassword = request.newPassword();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (authUser.getPassword() != null && !passwordEncoder.matches(currentPassword, authUser.getPassword())) {
            throw new BadCredentialsException("Wrong password provided.");
        }

        authUser.setPassword(passwordEncoder.encode(newPassword));

        authUser = userRepository.save(authUser);
        return userMapper.userToResponseData(authUser);
    }
    //TODO: create a delete user method
}
