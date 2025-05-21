package com.example.taskmanager.config.auth.config;

import com.example.taskmanager.config.auth.services.TokenService;
import com.example.taskmanager.user.entities.Account;
import com.example.taskmanager.user.entities.Session;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.entities.UserRolesEnum;
import com.example.taskmanager.user.repositories.AccountRepository;
import com.example.taskmanager.user.repositories.SessionRepository;
import com.example.taskmanager.user.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final SessionRepository sessionRepository;
    private final TokenService tokenService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) token.getPrincipal();
        String provider = token.getAuthorizedClientRegistrationId();

        if (provider.equals("google")) {
            authenticateWithGoogle(oAuth2User, response, token.getName());
        } else if (provider.equals("github")) {
            authenticateWithGithub(oAuth2User, response, token.getName());
        } else {
            return;
        }
    }

    private void authenticateUser(User user, HttpServletResponse response) throws IOException {
        Authentication token = new UsernamePasswordAuthenticationToken(user.getEmail(), false, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        String sessionToken = generateSessionToken();
        String accessToken = tokenService.generateToken(user.getEmail());
        Session session = Session.builder()
                .user(user)
                .sessionToken(sessionToken)
                .expiredAt(LocalDateTime.now().plusHours(24))
                .build();

        sessionRepository.save(session);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(300)
                .sameSite("Strict")
                .build();

        ResponseCookie sessionTokenCookie = ResponseCookie.from("session-token", sessionToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(24 * 3600)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", sessionTokenCookie.toString());
        response.sendRedirect("/api/auth/login-success");
    }

    private String generateSessionToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void authenticateWithGoogle(OAuth2User oAuth2User, HttpServletResponse response, String providerAccountId) throws IOException {
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");

        User user = userRepository.findByEmailWithAccounts(email).orElse(null);
        if (user != null) {
            Account account = accountRepository.findByProviderAndProviderAccountId("google", providerAccountId).orElse(null);
            if (account == null) {
                Account newAccount = Account.builder()
                        .provider("google")
                        .providerAccountId(providerAccountId)
                        .user(user)
                        .build();

                user.getAccounts().add(newAccount);
                accountRepository.save(newAccount);
            }
            authenticateUser(user, response);
            return;
        }

        User savedUser = userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .role(UserRolesEnum.USER)
                        .picture(picture)
                        .isEmailVerified(true)
                        .failedLoginAttempts(0)
                        .build()
        );

        accountRepository.save(
                Account.builder()
                        .provider("google")
                        .providerAccountId(providerAccountId)
                        .user(savedUser)
                        .build()
        );
        authenticateUser(savedUser, response);
    }

    private void authenticateWithGithub(OAuth2User oAuth2User, HttpServletResponse response, String providerAccountId) throws IOException {
        String name = (String) oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("avatar_url");

        User user = userRepository.findByEmailWithAccounts(email).orElse(null);
        if (user != null) {
            Account account = accountRepository.findByProviderAndProviderAccountId("github", providerAccountId).orElse(null);
            if (account == null) {
                Account newAccount = Account.builder()
                        .provider("github")
                        .providerAccountId(providerAccountId)
                        .user(user)
                        .build();

                user.getAccounts().add(newAccount);
                accountRepository.save(newAccount);
            }
            authenticateUser(user, response);
            return;
        }

        User savedUser = userRepository.save(
                User.builder()
                        .firstName(name)
                        .email(email)
                        .role(UserRolesEnum.USER)
                        .picture(picture)
                        .isEmailVerified(true)
                        .failedLoginAttempts(0)
                        .build()
        );

        accountRepository.save(
                Account.builder()
                        .provider("github")
                        .providerAccountId(providerAccountId)
                        .user(savedUser)
                        .build()
        );
        authenticateUser(savedUser, response);
    }
}
