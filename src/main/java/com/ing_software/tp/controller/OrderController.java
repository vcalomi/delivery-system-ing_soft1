package com.ing_software.tp.controller;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.model.Order;
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
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequest orderRequest,
                                              @RequestHeader("Authorization") String authorizationHeader){
        Order order = orderService.createOrder(orderRequest, authorizationHeader);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PatchMapping("/confirmOrder/{order_id}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long order_id){
        orderService.confirmOrder(order_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createRule")
    public ResponseEntity<OrderRule> createRule(@RequestBody Map<String, Object> ruleRequest){
        OrderRule rule = ruleService.createOrderRule(ruleRequest);
        return new ResponseEntity<>(rule, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getALlOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
