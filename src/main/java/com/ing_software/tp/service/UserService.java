package com.ing_software.tp.service;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.UserCredentials;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(UserRegisterRequest user) {
        User newUser = new User(null, user.getName(), user.getLastname(), user.getEmail(), user.getAge(),
                user.getAddress(), user.getUsername(), passwordEncoder.encode(user.getPassword()));
        UserDetails userDetails = userRepository.save(newUser);
        return jwtService.generateToken(userDetails.getUsername());
    }

    public LoginResponse loginUser(UserLoginRequest userCredentials){

        UserDetails userDetails = userRepository.findByUsername(userCredentials.getUsername());
        if (userDetails == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }

        if (passwordEncoder.matches(userCredentials.getPassword(), userDetails.getPassword())){
            String token = jwtService.generateToken(userDetails.getUsername());
            User user = (User)userDetails;
            return new LoginResponse(token,user.getName(), user.getLastname());
        }
        throw new UsernameNotFoundException("Invalid username or password");

    }
}
