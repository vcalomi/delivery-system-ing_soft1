package com.ing_software.tp.service;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;

public interface UserService {

    String registerUser(UserRegisterRequest user);
    LoginResponse loginUser(UserLoginRequest user);

    User findByUsername(String username);
}
