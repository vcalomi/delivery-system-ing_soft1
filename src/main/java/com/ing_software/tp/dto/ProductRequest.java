package com.ing_software.tp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank
    private Long Id;
    @NotBlank
    private String product_name;
    @NotBlank
    @Min(1)
    private int quantity;
}
