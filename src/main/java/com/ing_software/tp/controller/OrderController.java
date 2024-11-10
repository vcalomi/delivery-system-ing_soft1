package com.ing_software.tp.controller;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.AndRule;
import com.ing_software.tp.service.OrderService;
import com.ing_software.tp.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final RuleService ruleService;

    public OrderController(OrderService orderService, RuleService ruleService) {
        this.orderService = orderService;
        this.ruleService = ruleService;
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

    @PostMapping("/createRule")
    public ResponseEntity<Map<String, Object>> createRule(@RequestBody Map<String, Object> ruleRequest){
        ruleService.createOrderRule(ruleRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
