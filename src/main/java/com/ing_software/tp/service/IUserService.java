package com.ing_software.tp.service;

import com.ing_software.tp.model.User;
import org.apache.coyote.BadRequestException;

public interface IUserService {

    void registerUser(User user) throws BadRequestException;
}
