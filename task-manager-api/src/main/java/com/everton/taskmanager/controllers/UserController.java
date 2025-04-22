package com.everton.taskmanager.controllers;

import com.everton.taskmanager.entities.user.LoginUserDTO;
import com.everton.taskmanager.entities.user.RecoveryJwtTokenDTO;
import com.everton.taskmanager.entities.user.RecoveryUserDTO;
import com.everton.taskmanager.entities.user.UserToRegisterDTO;
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
    public ResponseEntity<RecoveryJwtTokenDTO> login(@RequestBody @Valid LoginUserDTO loginUserDTO) {
        return new ResponseEntity<>(userService.authenticateUser(loginUserDTO), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<RecoveryUserDTO> register(@RequestBody @Valid UserToRegisterDTO userToCreateDTO) {
        return new ResponseEntity<>(userService.createUser(userToCreateDTO), HttpStatus.CREATED);
    }
}
