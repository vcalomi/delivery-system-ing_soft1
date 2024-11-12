package com.ing_software.tp.dto;

import com.ing_software.tp.model.OrderProduct;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private Long orderId;
    private String username;
    private String email;
    @Size(min = 1, max = 100)
    private List<OrderProduct> products;
}
