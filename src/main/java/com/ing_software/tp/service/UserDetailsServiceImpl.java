package com.ing_software.tp.service;

import com.ing_software.tp.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);

        if(userDetails == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
    }
}
