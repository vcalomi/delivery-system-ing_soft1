package com.ing_software.tp.service;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegisterRequest user) {
        User newUser = new User(null, user.getName(), user.getLastname(), user.getEmail(), user.getAge(),
                user.getAddress());
        return userRepository.save(newUser);
    }
}
