package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;

import java.util.Map;

public interface OrderService {
    void createOrder(OrderRequest orderRequest);
    Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest);
}
