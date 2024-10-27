package com.ing_software.tp.service;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegisterRequest user) {
        User newUser = new User(null, user.getName(), user.getLastname(), user.getEmail(), user.getAge(),
                user.getAddress(), user.getUsername(), passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }
}
