package com.ing_software.tp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NewProductRequest {

    @NotBlank
    private String product_name;
    @Min(1)
    private int stock;
}
