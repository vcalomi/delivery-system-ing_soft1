package com.ing_software.tp.service;

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

    public void registerUser(User user) throws BadRequestException {
        if (user == null || user.getName() == null || user.getLastname() == null || user.getEmail() == null
        || user.getAddress() == null || user.getAge() <= 0) {
            throw new BadRequestException("Missing fields");
        }
        userRepository.save(user);
    }
}
