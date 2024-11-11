package com.ing_software.tp.service;

import com.ing_software.tp.dto.UserChangePasswordRequest;
import com.ing_software.tp.dto.UserForgetPasswordRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;

public interface UserService {

    String registerUser(UserRegisterRequest user);
    String loginUser(UserLoginRequest user) throws Exception;
    User findByUsername(String username);
    void generateNewPassword(UserForgetPasswordRequest user) throws Exception;
    void changePassword(UserChangePasswordRequest user) throws Exception;
}
