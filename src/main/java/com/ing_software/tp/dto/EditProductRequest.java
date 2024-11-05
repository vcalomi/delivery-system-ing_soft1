package com.ing_software.tp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class EditProductRequest {
    @Min(1)
    private Long id;
    @NotNull
    private Map<String, String> attributes;
}
