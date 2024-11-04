package com.ing_software.tp.service;

import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.User;
import jakarta.validation.Valid;

public interface UserService {

    String registerUser(UserRegisterRequest user);
    LoginResponse loginUser(UserLoginRequest user);
    User findByUsername(String username);
    void generateNewPassword(UserForgetPasswordRequest user);
    void changePassword(UserChangePasswordRequest user);
}
