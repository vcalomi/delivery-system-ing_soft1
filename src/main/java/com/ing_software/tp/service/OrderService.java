package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.OrderResponse;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.OrderStatus;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderCreateResponse createOrder(OrderRequest orderRequest, String authorizationHeader);
    Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest);

    void confirmOrder(Long orderId);

    List<OrderResponse> getAllOrders() throws Exception;

    List<OrderResponse> getUserOrders(String authorizationHeader) throws Exception;

    void cancelOrder(Long orderId);

    void changeOrderStatus(Long order_id, OrderStatus status);
}
