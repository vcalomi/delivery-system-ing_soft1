package com.ing_software.tp.controllerTests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing_software.tp.model.Order;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.*;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.EmailSenderService;
import com.ing_software.tp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderChangeStatusEndpointTest {
    private static final String LOGIN_URI = "/api/users/login";
    private static final String ORDERS_URI = "/api/orders";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    OrderService orderService;

    @MockBean
    EmailSenderService emailSenderService;

    static String userToken;
    static String adminToken;
    static Product[] products;
    static Order[] orders;

    @BeforeAll
    static void setUp(@Autowired TestRestTemplate restTemplate, @Autowired UserRepository userRepository, @Autowired ProductRepository productRepository, @Autowired OrderRepository orderRepository) {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        UserRegisterRequest adminRegisterRequest = new UserRegisterRequest("John", "Doe", "johndoe@email.com", 32,
                "address", "john", "password",  "M");
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

        products = new Product[]{new Product(1L, "product_1", 5, new HashMap<>()), new Product(2L, "product_2", 5, new HashMap<>()),
                new Product(3L, "product_3", 5, new HashMap<>()), new Product(4L, "product_4", 5, new HashMap<>())};
        productRepository.saveAll(Arrays.asList(products));

        List<OrderProduct> products1 = new ArrayList<>();
        products1.add(new OrderProduct(1L, 1L, "product_1", 1 , new HashMap<>()));
        products1.add(new OrderProduct(2L, 2L, "product_2", 1 , new HashMap<>()));

        List<OrderProduct> products2 = new ArrayList<>();
        products2.add(new OrderProduct(3L, 3L, "product_3", 1 , new HashMap<>()));
        products2.add(new OrderProduct(4L, 4L, "product_4", 1 , new HashMap<>()));

        orders = new Order[]{ new Order(1L, normalUser, products1, OrderStatus.CREATED, LocalDateTime.now()),
                new Order(2L, normalUser, products1, OrderStatus.CREATED, LocalDateTime.now()),
                new Order(3L, normalUser, products2, OrderStatus.CREATED, LocalDateTime.now()),
                new Order(4L, adminUser, products2, OrderStatus.CREATED, LocalDateTime.now())
        };
        orderRepository.saveAll(Arrays.asList(orders));
    }

    @Test
    void canConfirmOrder(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/confirmOrder/1", ORDERS_URI), HttpMethod.PATCH, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void canCancelOrder(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/cancel/2", ORDERS_URI), HttpMethod.DELETE, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void canChangeOrderStatus() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        Map<String, String> request = new HashMap<>();
        request.put("orderStatus", "SENT");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(request);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/changeStatus/3", ORDERS_URI), HttpMethod.PATCH, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cantChangeOrderStatusAsUser() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        Map<String, String> request = new HashMap<>();
        request.put("orderStatus", "SENT");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(request);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/changeStatus/3", ORDERS_URI), HttpMethod.PATCH, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    void cantChangeOrderStatusBadRequest() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        Map<String, String> request = new HashMap<>();
        request.put("orderStatus", "badRequest");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(request);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/changeStatus/3", ORDERS_URI), HttpMethod.PATCH, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
