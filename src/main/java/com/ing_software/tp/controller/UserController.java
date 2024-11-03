package com.ing_software.tp.controller;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest user){
        String token = userService.registerUser(user);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest userCredentials) {

        String token = userService.loginUser(userCredentials);
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
}
