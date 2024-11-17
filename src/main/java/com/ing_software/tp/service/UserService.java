package com.ing_software.tp.service;

import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.User;

public interface UserService {

    String registerUser(UserRegisterRequest user);
    String loginUser(UserLoginRequest user) throws Exception;
    User findByUsername(String username);
    void generateNewPassword(UserForgetPasswordRequest user) throws Exception;
    void changePassword(UserChangePasswordRequest user) throws Exception;
    void uploadProfilePicture(String authorizationHeader, byte[] pictureData) throws Exception;
    byte[] getProfilePicture(String authorizationHeader) throws Exception;
    UserData getUserData(String authorizationHeader);
}
