package com.ing_software.tp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserChangePasswordRequest {
    @NotBlank
    String username;
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
    @NotBlank
    String repeatedNewPassword;
}
