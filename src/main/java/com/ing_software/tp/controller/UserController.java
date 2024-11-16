package com.ing_software.tp.controller;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.User;
import com.ing_software.tp.service.UserService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;


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
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest userCredentials) throws Exception {

        String token = userService.loginUser(userCredentials);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PatchMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody @Valid UserForgetPasswordRequest userCredentials) throws Exception {
        userService.generateNewPassword(userCredentials);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid UserChangePasswordRequest userCredentials) throws Exception {
        userService.changePassword(userCredentials);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/profilePicture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestBody Map<String, String> request) throws Exception {

        String base64Image = request.get("profilePicture");
        userService.uploadProfilePicture(authorizationHeader, base64Image.getBytes());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        byte[] pictureData = userService.getProfilePicture(authorizationHeader);
        return ResponseEntity.ok(pictureData);
    }

}
