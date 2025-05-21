package com.example.taskmanager.user.services;

import com.example.taskmanager.config.auth.services.TokenService;
import com.example.taskmanager.user.dtos.CreateUserRequest;
import com.example.taskmanager.user.dtos.UserResponse;
import com.example.taskmanager.user.entities.*;
import com.example.taskmanager.user.jobs.SendEmailJob;
import com.example.taskmanager.user.mapper.UserMapper;
import com.example.taskmanager.user.repositories.UserRepository;
import com.example.taskmanager.user.repositories.VerificationTokenRepository;
import com.example.taskmanager.utils.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
                .expiredAt(LocalDateTime.now().plusMinutes(1))
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
    //TODO: create a get user method
    //TODO: create a update user method
    //TODO: create a delete user method
}
