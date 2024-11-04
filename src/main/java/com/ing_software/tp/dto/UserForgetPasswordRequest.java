package com.ing_software.tp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForgetPasswordRequest {
    @NotBlank
    String username;
    @Email
    @NotBlank
    String email;
}
