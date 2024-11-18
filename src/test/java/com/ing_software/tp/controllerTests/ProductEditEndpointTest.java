package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.*;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductEditEndpointTest {
    private static final String PRODUCTS_URI = "/api/products";
    private static final String LOGIN_URI = "/api/users/login";

    @Autowired
    TestRestTemplate restTemplate;

    static String adminToken;
    static String userToken;
    static Long product_id;

    @BeforeAll
    static void setUp(@Autowired TestRestTemplate restTemplate, @Autowired UserRepository userRepository, @Autowired ProductRepository productRepository, @Autowired OrderRepository orderRepository) {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        UserRegisterRequest adminRegisterRequest = new UserRegisterRequest("John", "Doe", "johndoe@email.com", 32,
                "address", "john", "password", "M");
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("Marta", "Rodriguez", "mrodriguez@email" +
                ".com", 28, "address2", "marta", "1234", "F");

        restTemplate.postForEntity("/api/users/register", adminRegisterRequest, String.class);
        restTemplate.postForEntity("/api/users/register", userRegisterRequest, String.class);

        User adminUser = (User) userRepository.findByUsername("john");
        User normalUser = (User) userRepository.findByUsername("marta");

        adminUser.setRole("ADMIN");
        normalUser.setRole("USER");

        userRepository.save(adminUser);
        userRepository.save(normalUser);

        UserLoginRequest adminLoginRequest = new UserLoginRequest("john", "password");
        UserLoginRequest userLoginRequest = new UserLoginRequest("marta", "1234");

        ResponseEntity<String> adminResponse = restTemplate.postForEntity(LOGIN_URI, adminLoginRequest, String.class);
        ResponseEntity<String> userResponse = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);

        adminToken = adminResponse.getBody();
        userToken = userResponse.getBody();
        Product product = productRepository.save(new Product(null, "product_1", 5, new HashMap<>()));
        product_id = product.getId();
    }

    @Test
    void canEditProduct() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "red");
        EditProductRequest editProductRequest = new EditProductRequest(product_id, attributes);
        HttpEntity<EditProductRequest> requestEntity = new HttpEntity<>(editProductRequest, headers);
        ResponseEntity<?> response = restTemplate.exchange(String.format("%s/edit", PRODUCTS_URI), HttpMethod.PATCH, requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cantEditProductAsUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "red");
        EditProductRequest editProductRequest = new EditProductRequest(product_id, attributes);
        HttpEntity<EditProductRequest> requestEntity = new HttpEntity<>(editProductRequest, headers);
        ResponseEntity<?> response = restTemplate.exchange(String.format("%s/edit", PRODUCTS_URI), HttpMethod.PATCH, requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void canIncrementStock(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        IncrementStockRequest incrementStockRequest = new IncrementStockRequest(1);
        HttpEntity<IncrementStockRequest> requestEntity = new HttpEntity<>(incrementStockRequest, headers);
        ResponseEntity<?> response = restTemplate.exchange(String.format("%s/incrementStock/%,d", PRODUCTS_URI, product_id),
                HttpMethod.PATCH, requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cantIncrementStockAsUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        IncrementStockRequest incrementStockRequest = new IncrementStockRequest(1);
        HttpEntity<IncrementStockRequest> requestEntity = new HttpEntity<>(incrementStockRequest, headers);
        ResponseEntity<?> response = restTemplate.exchange(String.format("%s/incrementStock/%,d", PRODUCTS_URI, product_id),
                HttpMethod.PATCH, requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
