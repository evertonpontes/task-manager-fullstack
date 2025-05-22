package com.example.taskmanager.user.controllers;

import com.example.taskmanager.user.dtos.AuthTokensResponse;
import com.example.taskmanager.user.dtos.LoginUserRequest;
import com.example.taskmanager.user.dtos.UserResponse;
import com.example.taskmanager.user.services.AuthService;
import com.example.taskmanager.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        AuthTokensResponse tokensResponse = authService.login(request);
        UserResponse user = userService.findByAccessToken(tokensResponse.accessToken());

        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", tokensResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(300)
                .sameSite("Strict")
                .build();

        ResponseCookie sessionTokenCookie = ResponseCookie.from("session-token", tokensResponse.sessionToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(5 * 3600)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, sessionTokenCookie.toString())
                .body(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@CookieValue("session-token") String sessionToken) {
        AuthTokensResponse tokensResponse = authService.refreshToken(sessionToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", tokensResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(300)
                .sameSite("Strict")
                .build();

        ResponseCookie sessionTokenCookie = ResponseCookie.from("session-token", tokensResponse.sessionToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(5 * 3600)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, sessionTokenCookie.toString())
                .build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, @CookieValue("session-token") String sessionToken) {
        authService.logout(request, response, sessionToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie sessionTokenCookie = ResponseCookie.from("session-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, sessionTokenCookie.toString())
                .build();
    }
}
