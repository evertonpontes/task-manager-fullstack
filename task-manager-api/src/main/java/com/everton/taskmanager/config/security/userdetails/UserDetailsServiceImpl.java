package com.everton.taskmanager.config.security.userdetails;

import com.everton.taskmanager.entities.users.User;
import com.everton.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println(username);

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return new UserDetailsImpl(user);
    }
}
