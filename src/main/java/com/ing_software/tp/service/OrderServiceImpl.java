package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final  UserService userService;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, JwtService jwtService, UserService userService) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public Order createOrder(OrderRequest orderRequest, String authorizationHeader) {

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
            return orderRepository.save(order);
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
}
