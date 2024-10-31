package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;
import org.apache.coyote.BadRequestException;

import java.util.Map;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest);
}
