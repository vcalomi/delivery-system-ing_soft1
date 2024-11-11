package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderConfirmedResponse;
import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final  UserService userService;
    private final EmailSenderService emailSenderService;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, JwtService jwtService, UserService userService, EmailSenderService emailSenderService) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    public OrderCreateResponse createOrder(@Valid OrderRequest orderRequest, String authorizationHeader) {

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

        User user = userService.findByUsername(username);

        Map<ProductRequest, Boolean> nonValidProductsRequested = validateOrderRequestStock(orderRequest);
        if (nonValidProductsRequested.isEmpty()){
            Order order = new Order();
            List<Product> products = new ArrayList<>();

            for (ProductRequest productRequest: orderRequest.getProducts()){
                Optional<Product> optionalProduct = productService.findProductById(productRequest.getId());
                optionalProduct.ifPresent(products::add);
            }
            order.setProducts(products);
            order.setOwner(user);
            orderRepository.save(order);

            return new OrderCreateResponse(order.getId(), user.getUsername(), order.getProducts());
        }
        throw new RuntimeException("Invalid order");
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
        orderRepository.save(order.get());
        emailSenderService.sendConfirmationEmail(user.getEmail(),"Confirmation Email", emailSenderService.buildOrderConfirmationEmail(order.get()));
    }

    public List<OrderConfirmedResponse> getConfirmedOrders() throws Exception {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        List<OrderConfirmedResponse> confirmedOrders = new ArrayList<>();
        if (orders.isEmpty()) {
            throw new Exception("No orders found");
        }
        for (Order order: orders){
            if(order.isConfirmed()){
                OrderConfirmedResponse confirmedOrder = new OrderConfirmedResponse(order.getId(),
                        order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts());
                confirmedOrders.add(confirmedOrder);
            }
        }
        if (confirmedOrders.isEmpty()){
            throw new Exception("No confirmed orders found");
        }
        return confirmedOrders;
    }
}
