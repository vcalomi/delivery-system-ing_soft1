package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderResponse;
import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderCreateResponse createOrder(OrderRequest orderRequest, String authorizationHeader);
    Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest);

    void confirmOrder(Long orderId);

    List<OrderResponse> getConfirmedOrders(String sortBy) throws Exception;

    List<OrderResponse> getOrders(String sortBy, String authorizationHeader) throws Exception;

    void cancelOrder(Long orderId);
}
