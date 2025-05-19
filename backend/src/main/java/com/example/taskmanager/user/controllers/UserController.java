package com.example.taskmanager.user.controllers;

import com.example.taskmanager.user.dtos.CreateUserRequest;
import com.example.taskmanager.user.dtos.UserResponse;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(userService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("verify-email")
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

    @GetMapping("login")
    public String login() {
        return "Hello world";
    }
}
