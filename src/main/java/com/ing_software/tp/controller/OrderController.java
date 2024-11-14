package com.ing_software.tp.controller;

import com.ing_software.tp.dto.OrderResponse;
import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.service.OrderService;
import com.ing_software.tp.service.RuleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final RuleServiceImpl ruleService;

    public OrderController(OrderService orderService, RuleServiceImpl ruleService) {
        this.orderService = orderService;
        this.ruleService = ruleService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest,
                                                           @RequestHeader("Authorization") String authorizationHeader){
        OrderCreateResponse order = orderService.createOrder(orderRequest, authorizationHeader);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PatchMapping("/confirmOrder/{order_id}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long order_id){
        orderService.confirmOrder(order_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getConfirmedOrders(@RequestParam(required = false) String sortBy) throws Exception{
        List<OrderResponse> confirmedOrders = orderService.getConfirmedOrders(sortBy);
        return new ResponseEntity<>(confirmedOrders, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false ) String sortBy){
        List<OrderResponse> orders = orderService.getOrders(sortBy, authorizationHeader);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{order_id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long order_id){
        orderService.cancelOrder(order_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/createRule")
    public ResponseEntity<OrderRule> createRule(@RequestBody Map<String, Object> ruleRequest){
        OrderRule rule = ruleService.createOrderRule(ruleRequest);
        return new ResponseEntity<>(rule, HttpStatus.CREATED);
    }



}
