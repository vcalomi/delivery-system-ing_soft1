package com.ing_software.tp;

import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    String BASE_URL;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    static String token;

    @BeforeAll
    static void registerAnUser(@Autowired TestRestTemplate restTemplate, @LocalServerPort int port) {
        // Configurar la URL base y el usuario
        String baseUrl = "http://localhost:" + port + "/api/users/register";

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("John", "Doe", "johndoe@email.com", 32,
                "address", "jon", "1234");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl,
                userRegisterRequest, String.class);

        token = response.getBody();
    }

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/products"; // Actualiza BASE_URL según el puerto aleatorio
    }

    @Test
    void canCreateAProduct(){
        NewProductRequest newProduct = new NewProductRequest("product_1", 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(BASE_URL + "/create", requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void cantCreateAProductWithANullName(){
        NewProductRequest newProduct = new NewProductRequest(null, 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(BASE_URL + "/create", requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantCreateAProductWithAnEmptyName(){
        NewProductRequest newProduct = new NewProductRequest("", 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(BASE_URL + "/create", requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantCreateAProductWithLessThanOneOfStock(){
        NewProductRequest newProduct = new NewProductRequest("product_1", 0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(BASE_URL + "/create", requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
