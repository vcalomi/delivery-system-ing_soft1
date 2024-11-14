package com.ing_software.tp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewProductRequest {

    @NotBlank
    private String product_name;
    @Min(1)
    private int stock;
    private Map<String,String> attributes;
}
