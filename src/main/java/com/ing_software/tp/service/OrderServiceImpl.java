package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.OrderRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderRequest orderRequest) {
        Map<ProductRequest, Boolean> nonValidProductsRequested = validateOrderRequestStock(orderRequest);
        if (nonValidProductsRequested.isEmpty()){
            Order order = new Order();
            List<Product> products = new ArrayList<>();

            for (ProductRequest productRequest: orderRequest.getProducts()){
                Optional<Product> optionalProduct = productService.findProductById(productRequest.getId());
                optionalProduct.ifPresent(products::add);
            }
            order.setProducts(products);
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
