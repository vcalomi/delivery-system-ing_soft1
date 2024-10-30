package com.ing_software.tp.controller;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }


}
