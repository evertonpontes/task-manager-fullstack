package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.user.CreateUserDTO;
import com.everton.taskmanager.dtos.user.LoginDTO;
import com.everton.taskmanager.dtos.user.AuthenticationResponseDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        AuthenticationResponseDTO auth = userService.authenticateUser(loginDTO);

        ResponseCookie responseCookie = ResponseCookie.from("access-token", auth.token())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(14400)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(auth.user());
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid CreateUserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser() {
        return new ResponseEntity<>(userService.getUserAuthenticated(), HttpStatus.OK);
    }
}
