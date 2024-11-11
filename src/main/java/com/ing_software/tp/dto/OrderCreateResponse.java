package com.ing_software.tp.dto;

import com.ing_software.tp.model.Product;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreateResponse {
    private Long id;
    private String username;
    @Size(min = 1, max = 100)
    private List<Product> products;
}
