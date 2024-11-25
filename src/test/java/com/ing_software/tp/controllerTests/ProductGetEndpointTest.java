package com.ing_software.tp.controllerTests;

import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductGetEndpointTest {

    private static final String PRODUCTS_URI = "/api/products/";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void deleteProducts() {
        productRepository.deleteAll();
    }

    @AfterAll
    static void cleanDatabase(@Autowired OrderProductRepository orderProductRepository,
                              @Autowired ProductRepository productRepository,
                              @Autowired UserRepository userRepository, @Autowired OrderRepository orderRepository) {
        productRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
        orderProductRepository.deleteAll();
    }

    @Test
    void getProductsWithNoProductsCreatedReturnsNotFound(){
        ResponseEntity<Void> response = restTemplate.getForEntity(PRODUCTS_URI, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getProductsWithFourProductsReturnsTheCorrectAmountOfProducts(){
        Product[] products = {new Product(null, "product_1", 5, new HashMap<>()), new Product(null, "product_2", 5, new HashMap<>()),
                new Product(null, "product_3", 5, new HashMap<>()),new Product(null, "product_4", 5, new HashMap<>())};
        productRepository.saveAll(Arrays.asList(products));

        ResponseEntity<List> response = restTemplate.getForEntity(PRODUCTS_URI, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Product> responseProducts = response.getBody();
        assertThat(responseProducts.size()).isEqualTo(4);
    }
}
