package com.ing_software.tp.controllerTests;

import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.ProductRepository;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductGetEndpointTest {

    private static final String PRODUCTS_URI = "/api/products/";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    @Test
    void getProductsWithNoProductsCreatedReturnsNotFound(){
        ResponseEntity<Void> response = restTemplate.getForEntity(PRODUCTS_URI, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getProductsWithFourProductsReturnsTheCorrectAmountOfProducts(){
        Map<String,String> attributes = new HashMap<>();
        Product[] products = {new Product(null, "product_1", 5,attributes), new Product(null, "product_2", 5,attributes),new Product(null,
                "product_3", 5,attributes),new Product(null, "product_4", 5,attributes)};
        productRepository.saveAll(Arrays.asList(products));

        ResponseEntity<List> response = restTemplate.getForEntity(PRODUCTS_URI, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Product> responseProducts = response.getBody();
        assertThat(responseProducts.size()).isEqualTo(4);
    }
}
