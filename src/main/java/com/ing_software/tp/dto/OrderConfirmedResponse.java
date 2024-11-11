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
public class OrderConfirmedResponse {
    private Long orderId;
    private String username;
    private String email;
    @Size(min = 1, max = 100)
    private List<Product> products;
}
