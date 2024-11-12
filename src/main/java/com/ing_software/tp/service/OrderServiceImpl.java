package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderResponse;
import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.*;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final RuleService ruleService;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, JwtService jwtService, UserService userService, EmailSenderService emailSenderService, RuleService ruleService, OrderProductRepository orderProductRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;

        this.ruleService = ruleService;
        this.orderProductRepository = orderProductRepository;
    }

    private String validateAuthorization(String authorizationHeader){
        String username = null;
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            username = jwtService.extractUsername(token);

        } else if (authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            username = values[0];
        }
        return username;
    }

    public OrderCreateResponse createOrder(@Valid OrderRequest orderRequest, String authorizationHeader) {

        String username = validateAuthorization(authorizationHeader);

        User user = userService.findByUsername(username);

        Map<ProductRequest, Boolean> nonValidProductsRequested = validateOrderRequestStock(orderRequest);
        if(!nonValidProductsRequested.isEmpty()) {
            throw new RuntimeException("Invalid order");
        }

        Order order = new Order();
        List<OrderProduct> products = new ArrayList<>();

        for (ProductRequest productRequest: orderRequest.getProducts()){

            Optional<Product> optionalProduct = productService.findProductById(productRequest.getId());
            Map<String, String> attributes = null;
            if (optionalProduct.isPresent()) {
                attributes = new HashMap<>(Map.copyOf(optionalProduct.get().getAttributes()));
            }

            OrderProduct product = new OrderProduct(null, optionalProduct.get().getId(), productRequest.getProduct_name(),
                    productRequest.getQuantity(), attributes);
            OrderProduct entityProduct = orderProductRepository.save(product);
            products.add(entityProduct);
        }
        order.setProducts(products);
        order.setOwner(user);

        List<OrderRule> rules = ruleService.getAllRules();
        for (OrderRule rule : rules) {
            if(!rule.isSatisfiedBy(order))
                throw new RuntimeException("Rule not satisfied!");
        }

        orderRepository.save(order);
        return new OrderCreateResponse(order.getId(), user.getUsername(), order.getProducts());
    }

    public Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest){
        Map<ProductRequest, Boolean> result = new HashMap<>();
        for (ProductRequest productRequest: orderRequest.getProducts()){
            if (!productService.validateStock(productRequest.getId(), productRequest.getQuantity())){
                result.put(productRequest, false);
            }
        }
        return result;
    }

    public void confirmOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()){
            throw new RuntimeException("No order with provided id");
        }
        User user = order.get().getOwner();
        order.get().setConfirmed(true);
        productService.updateStock(order.get().getProducts());
        orderRepository.save(order.get());
        emailSenderService.sendConfirmationEmail(user.getEmail(),"Confirmation Email", emailSenderService.buildOrderConfirmationEmail(order.get()));
    }

    public List<OrderResponse> getConfirmedOrders(String sortBy) throws Exception {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new Exception("No orders found");
        }
        if (Objects.equals(sortBy, "confirmed")) {
            List<OrderResponse> confirmedOrders = new ArrayList<>();
            for (Order order: orders){
                if(order.isConfirmed()){
                    OrderResponse confirmedOrder = new OrderResponse(order.getId(),
                            order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts());
                    confirmedOrders.add(confirmedOrder);
                }
            }
            if (confirmedOrders.isEmpty()){
                throw new Exception("No confirmed orders found");
            }
            return confirmedOrders;
        }
        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order: orders){
            OrderResponse confirmedOrder = new OrderResponse(order.getId(),
                        order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts());
            ordersResponse.add(confirmedOrder);
        }
        return ordersResponse;
    }
}
