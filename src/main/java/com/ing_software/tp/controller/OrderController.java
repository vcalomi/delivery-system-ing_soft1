package com.ing_software.tp.controller;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequest orderRequest,
                                              @RequestHeader("Authorization") String authorizationHeader){
        orderService.createOrder(orderRequest, authorizationHeader);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @PatchMapping("/confirmOrder/{order_id}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long order_id){
        orderService.confirmOrder(order_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
