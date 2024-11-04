package com.ing_software.tp.controller;

import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.User;
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
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest userCredentials) {
        LoginResponse response = userService.loginUser(userCredentials);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody @Valid UserForgetPasswordRequest userCredentials) {
        userService.generateNewPassword(userCredentials);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid UserChangePasswordRequest userCredentials) {
        userService.changePassword(userCredentials);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
