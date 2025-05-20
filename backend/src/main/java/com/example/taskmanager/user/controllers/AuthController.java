package com.example.taskmanager.user.controllers;

import com.example.taskmanager.user.dtos.AuthTokensResponse;
import com.example.taskmanager.user.dtos.LoginUserRequest;
import com.example.taskmanager.user.dtos.UserResponse;
import com.example.taskmanager.user.services.AuthService;
import com.example.taskmanager.user.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("login")
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
}
