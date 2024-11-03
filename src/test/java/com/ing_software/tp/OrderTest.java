package com.ing_software.tp;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void addProducts(){
        productRepository.save(new Product(1L, "product_1", 1));
        productRepository.save(new Product(2L, "product_2", 3));
    }

    @Test
    void canCreateAnOrderFromRequest(){

        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1L, "product_1", 1));
        ProductRequest product_2 = new ProductRequest(2L, "product_2", 2);
        productRequests.add(product_2);
        OrderRequest orderRequest = new OrderRequest(productRequests);
        assertThat(orderService.validateOrderRequestStock(orderRequest)).isEmpty();

    }

    @Test
    void cantCreateAnOrderFromRequestIfStockOfAProductIsLessThanQuantityWanted(){

        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1L, "product_1", 1));
        ProductRequest product_2 = new ProductRequest(2L, "product_2", 5);
        productRequests.add(product_2);
        OrderRequest orderRequest = new OrderRequest(productRequests);
        assertThat(orderService.validateOrderRequestStock(orderRequest).get(product_2)).isFalse();

    }



}
