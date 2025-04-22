package com.everton.taskmanager.services;

import com.everton.taskmanager.entities.user.*;
import com.everton.taskmanager.exceptions.AlreadyExistsException;
import com.everton.taskmanager.exceptions.UserNotFoundException;
import com.everton.taskmanager.mapper.UserMapper;
import com.everton.taskmanager.repositories.UserRepository;
import com.everton.taskmanager.security.JwtTokenService;
import com.everton.taskmanager.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    public RecoveryJwtTokenDTO authenticateUser(LoginUserDTO loginUserDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.password());

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User userDetails = (User) auth.getPrincipal();

        return new RecoveryJwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

    public RecoveryUserDTO createUser(UserToRegisterDTO userDTO) {

        if (userRepository.findUserByEmail(userDTO.email()) != null) throw new AlreadyExistsException("Email address already registered.");

        User newUser = User
                .builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(securityConfiguration.passwordEncoder().encode(userDTO.password()))
                .build();

        User userSaved = userRepository.save(newUser);

        return userMapper.userToRecoveryUserDTO(userSaved);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(username);

        if (user == null) throw new UserNotFoundException();

        return user;
    }
}
