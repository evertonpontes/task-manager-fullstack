package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.user.CreateUserDTO;
import com.everton.taskmanager.dtos.user.LoginDTO;
import com.everton.taskmanager.dtos.user.TokenDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.authenticateUser(loginDTO), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid CreateUserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }
}
