package com.ing_software.tp.service;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import org.apache.coyote.BadRequestException;

public interface IUserService {

    User registerUser(UserRegisterRequest user);
}
