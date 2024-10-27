package com.ing_software.tp.controller;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
