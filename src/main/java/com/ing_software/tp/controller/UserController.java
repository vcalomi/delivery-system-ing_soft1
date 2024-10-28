package com.ing_software.tp.controller;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest user){
        String token = userService.registerUser(user);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest userCredentials) {

        LoginResponse response = userService.loginUser(userCredentials);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
