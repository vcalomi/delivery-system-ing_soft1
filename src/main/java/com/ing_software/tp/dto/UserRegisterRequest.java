package com.ing_software.tp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    @Email
    @NotBlank
    private String email;
    @Min(18)
    @Max(100)
    private int age;
    @NotBlank
    private String address;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
