package com.everton.taskmanager.services;

import com.everton.taskmanager.config.exceptions.UnauthorizedException;
import com.everton.taskmanager.config.security.userdetails.UserDetailsImpl;
import com.everton.taskmanager.entities.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated.");
        }

        return authentication;
    }

    public UserDetailsImpl getUserDetails() {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new UnauthorizedException("Invalid user authentication.");
        }

        return (UserDetailsImpl) principal;
    }

    public User getAuthenticatedUser() {
        return getUserDetails().getUser();
    }
}
