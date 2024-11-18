package com.ing_software.tp.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(String username);

    Boolean validateToken(String token, UserDetails userDetails);

    String extractUsername(String token);

    String validateAuthorization(String authorizationHeader);
}
