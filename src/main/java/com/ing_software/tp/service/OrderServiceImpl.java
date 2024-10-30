package com.ing_software.tp.service;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;

    public OrderServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    public void createOrder(OrderRequest orderRequest) {

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
