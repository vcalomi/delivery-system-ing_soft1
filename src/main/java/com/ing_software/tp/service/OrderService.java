package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;

import java.util.Map;

public interface OrderService {
    OrderCreateResponse createOrder(OrderRequest orderRequest, String authorizationHeader);
    Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest);

    void confirmOrder(Long orderId);
}
