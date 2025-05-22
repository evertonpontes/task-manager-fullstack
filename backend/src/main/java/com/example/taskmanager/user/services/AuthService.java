package com.example.taskmanager.user.services;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.taskmanager.config.auth.services.TokenService;
import com.example.taskmanager.user.dtos.AuthTokensResponse;
import com.example.taskmanager.user.dtos.LoginUserRequest;
import com.example.taskmanager.user.entities.Session;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.repositories.SessionRepository;
import com.example.taskmanager.user.repositories.UserRepository;
import com.example.taskmanager.utils.exceptions.CustomAuthenticationException;
import com.example.taskmanager.utils.exceptions.TokenValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AuthTokensResponse login(LoginUserRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        try {
            isAccountLocked(email);
            Authentication token = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(token);
            User user = (User) authentication.getPrincipal();

            clearFailedLoginAttempts(user);

            String sessionToken = generateSessionToken();
            String accessToken = tokenService.generateToken(user.getEmail());
            Session session = Session.builder()
                    .user(user)
                    .sessionToken(sessionToken)
                    .expiredAt(LocalDateTime.now().plusHours(24))
                    .build();

            user.getSessions().add(session);
            sessionRepository.save(session);
            return new AuthTokensResponse(accessToken, sessionToken);

        } catch (BadCredentialsException e) {
            Optional<User> userOptional = userRepository.findByEmail(email);

            userOptional.ifPresent(user -> {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                user.setLastFailedLogin(LocalDateTime.now());
                if (user.getFailedLoginAttempts() >= 10) {
                    user.setIsLockedUntil(LocalDateTime.now().plusSeconds(30));
                }
                userRepository.save(user);
            });
            throw new CustomAuthenticationException("Invalid email or password.");
        } catch (DisabledException e) {
            throw new CustomAuthenticationException("You must verify your email before logging in.");
        } catch (AuthenticationException e) {
            if (e instanceof LockedException) throw new CustomAuthenticationException(e.getMessage());

            throw new CustomAuthenticationException("Authentication failed.");
        }
    }

    private void isAccountLocked(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        userOptional.ifPresent(user -> {
            if (user.getIsLockedUntil() != null && user.getIsLockedUntil().isAfter(LocalDateTime.now())) {
                Duration secondsLeft = Duration.between(LocalDateTime.now(), user.getIsLockedUntil());
                throw new LockedException("Your account is locked, try to login in %d seconds".formatted(secondsLeft.getSeconds()));
            }
        });
    }

    private void clearFailedLoginAttempts(User user) {
        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            if (user.getIsLockedUntil() != null) {
                user.setIsLockedUntil(null);
            }
            userRepository.save(user);
        }
    }

    private String generateSessionToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public AuthTokensResponse refreshToken(String sessionToken) {
        Session session = sessionRepository.findBySessionToken(sessionToken).orElseThrow(() -> new TokenValidateException("Invalid token provided."));

        if (session.getExpiredAt().isBefore(LocalDateTime.now())) throw  new TokenExpiredException("Session token expired.", session.getExpiredAt().toInstant(ZoneOffset.of("-03:00")));

        String newSessionToken = generateSessionToken();
        User user = session.getUser();
        String accessToken = tokenService.generateToken(user.getEmail());
        Session newSession = sessionRepository.save(
                Session.builder()
                        .user(user)
                        .sessionToken(newSessionToken)
                        .expiredAt(LocalDateTime.now().plusHours(24))
                        .build()
        );

        sessionRepository.delete(session);
        return new AuthTokensResponse(
                accessToken,
                newSessionToken
        );
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, String sessionToken) {
        sessionRepository.deleteBySessionToken(sessionToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.logoutHandler.logout(request, response, authentication);
    }
}
