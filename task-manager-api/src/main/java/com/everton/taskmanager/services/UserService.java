package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.AlreadyExistsException;
import com.everton.taskmanager.config.security.JwtTokenService;
import com.everton.taskmanager.config.security.userdetails.UserDetailsImpl;
import com.everton.taskmanager.dtos.user.CreateUserDTO;
import com.everton.taskmanager.dtos.user.LoginDTO;
import com.everton.taskmanager.dtos.user.TokenDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.mapper.UserMapper;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    public UserResponseDTO createUser(CreateUserDTO userDTO) {

        String email = userDTO.email().trim().toLowerCase();
        String name = userDTO.name().trim();

        if (userRepository.findByEmail(email).isPresent()) throw new AlreadyExistsException("The Provided email is already registered.");

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(userDTO.password()))
                .build();

        return userMapper.userToUserResponseDTO(userRepository.save(user));
    }

    public TokenDTO authenticateUser(LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new TokenDTO(jwtTokenService.generateToken(userDetails));
    }
}
