package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.EmailSenderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductCreateEndpointTest {
    private static final String PRODUCTS_URI = "/api/products";
    private static final String LOGIN_URI = "/api/users/login";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    EmailSenderService emailSenderService;

    static String adminToken;
    static String userToken;

    @BeforeAll
    static void registerAnUser(@Autowired TestRestTemplate restTemplate, @Autowired UserRepository userRepository) {
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
    }


    @Test
    void userCantCreateAProduct(){
        Map<String,String> attributes = new HashMap<>();
        NewProductRequest newProduct = new NewProductRequest("product_1", 20,attributes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", userToken));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity,
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void adminCanCreateAProduct(){
        Map<String,String> attributes = new HashMap<>();
        NewProductRequest newProduct = new NewProductRequest("product_1", 20,attributes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity,
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void cantCreateAProductWithANullName(){
        Map<String,String> attributes = new HashMap<>();
        NewProductRequest newProduct = new NewProductRequest(null, 20,attributes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantCreateAProductWithAnEmptyName(){
        Map<String,String> attributes = new HashMap<>();
        NewProductRequest newProduct = new NewProductRequest("", 20,attributes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", adminToken));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
