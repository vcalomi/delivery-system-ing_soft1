package com.ing_software.tp.service;

import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSenderService emailSenderService;
    private final PasswordGeneratorService passwordGeneratorService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailSenderService emailSenderService, PasswordGeneratorService passwordGeneratorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailSenderService = emailSenderService;
        this.passwordGeneratorService = passwordGeneratorService;
    }

    private String createUser(@Valid UserRegisterRequest user) {
        User newUser = new User(null, user.getName(), user.getLastname(), user.getEmail(), user.getAge(),
                user.getAddress(), user.getUsername(), passwordEncoder.encode(user.getPassword()), "ADMIN");
        UserDetails userDetails = userRepository.save(newUser);
        return jwtService.generateToken(userDetails.getUsername());
    }

    public String registerUser(@Valid UserRegisterRequest user) {
        String token = this.createUser(user);
        emailSenderService.sendConfirmationEmail(user.getEmail(),"Confirmation Email","Registro con exito");
        return token;
    }

    public String loginUser(@Valid UserLoginRequest userCredentials) throws Exception {

        UserDetails userDetails = userRepository.findByUsername(userCredentials.getUsername());
        if (userDetails == null){
            throw new Exception("Invalid username.");
        }

        if (passwordEncoder.matches(userCredentials.getPassword(), userDetails.getPassword())){
            return jwtService.generateToken(userDetails.getUsername());
        }
        throw new Exception("Invalid password");
    }

    public User findByUsername(String username) {
        return (User) userRepository.findByUsername(username);
    }

    public void generateNewPassword(@Valid UserForgetPasswordRequest userCredentials) throws Exception {
        UserDetails userDetails = userRepository.findByUsername(userCredentials.getUsername());
        if (userDetails == null){
            throw new Exception("Invalid username.");
        }
        String randomPassword = passwordGeneratorService.generateRandomPassword();
        User user = (User) userDetails;
        emailSenderService.sendConfirmationEmail(user.getEmail(),"New Password","Tu nueva contraseña es: " + randomPassword);
        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);
    }

    public void changePassword(@Valid UserChangePasswordRequest userCredentials) throws Exception {
        UserDetails userDetails = userRepository.findByUsername(userCredentials.getUsername());
        if (userDetails == null){
            throw new Exception("Invalid username.");
        }
        if (passwordEncoder.matches(userCredentials.getOldPassword(), userDetails.getPassword()) &&
                userCredentials.getNewPassword().equals(userCredentials.getRepeatedNewPassword()) ){
            User user = (User) userDetails;
            user.setPassword(passwordEncoder.encode(userCredentials.getNewPassword()));
            userRepository.save(user);
        }
        else if(!userCredentials.getNewPassword().equals(userCredentials.getRepeatedNewPassword())){
            throw new Exception("New password and Repeated New Password do not match.");
        }
        else{
            throw new Exception("Invalid password.");
        }
    }
}
