package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderCreateEndpointTest {
    private static final String PRODUCTS_URI = "/api/products";
    private static final String LOGIN_URI = "/api/users/login";
    private static final String ORDERS_URI = "/api/orders";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    OrderService orderService;

    static String userToken;
    static String adminToken;
    static Product[] products;

    @BeforeAll
    static void registerAnUser(@Autowired TestRestTemplate restTemplate, @Autowired UserRepository userRepository, @Autowired ProductRepository productRepository) {
        UserRegisterRequest adminRegisterRequest = new UserRegisterRequest("John", "Doe", "johndoe@email.com", 32, "address", "john", "password");
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("Marta", "Rodriguez", "mrodriguez@email.com", 28, "address2", "marta", "1234");

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

        products = new Product[]{new Product(1L, "product_1", 5, new HashMap<>()), new Product(2L, "product_2", 5,
                new HashMap<>()),
                new Product(3L, "product_3", 5, new HashMap<>()), new Product(4L, "product_4", 5, new HashMap<>())};
        productRepository.saveAll(Arrays.asList(products));
    }

    @Test
    void canCreateAnOrder(){
        List<ProductRequest> productRequests = new ArrayList<>();

        productRequests.add(new ProductRequest(1L, "product_1", 5));
        productRequests.add(new ProductRequest(2L, "product_2", 5));

        OrderRequest orderRequest = new OrderRequest(productRequests);
        assertThat(orderService.validateOrderRequestStock(orderRequest)).isEmpty();
    }

    @Test
    void cantCreateAnOrderWithMoreProductsThanAvailable(){
        List<ProductRequest> productRequests = new ArrayList<>();

        productRequests.add(new ProductRequest(1L, "product_1", 10));
        productRequests.add(new ProductRequest(2L, "product_2", 5));

        OrderRequest orderRequest = new OrderRequest(productRequests);
        assertThat(orderService.validateOrderRequestStock(orderRequest)).isNotEmpty();
    }

    @Test
    void cantCreateAnEmptyOrder(){
        List<ProductRequest> productRequests = new ArrayList<>();
        OrderRequest orderRequest = new OrderRequest(productRequests);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", ORDERS_URI), requestEntity,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
