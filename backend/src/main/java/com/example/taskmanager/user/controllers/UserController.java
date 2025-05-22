package com.example.taskmanager.user.controllers;

import com.example.taskmanager.user.dtos.*;
import com.example.taskmanager.user.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(userService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/verify-email")
    public RedirectView verifyToken(@RequestParam String token) {
        try {
            userService.verifyToken(token);
            String message = "Email verified successfully!";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "http://localhost:8080/api/auth/login?message=" + encodedMessage;
            return new RedirectView(url);
        } catch (Exception e) {
            String message = e.getMessage();
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "http://localhost:8080/api/auth/login?error=" + encodedMessage;
            return new RedirectView(url);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(token, request);
        return ResponseEntity.ok().build();
    }

   @PutMapping("/profile")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok().body(userService.update(request));
    }

    @PatchMapping("/password")
    public ResponseEntity<UserResponse> updatePassword(@Valid @RequestBody UpdateUserPassword request) {
        return ResponseEntity.ok().body(userService.updatePassword(request));
    }
}
