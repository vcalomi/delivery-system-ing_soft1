package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.UserRegisterRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductCreateEndpointTest {
    private static final String PRODUCTS_URI = "/api/products";
    private static final String REGISTER_URI = "/api/users/register";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    EmailSenderService emailSenderService;

    static String token;

    @BeforeAll
    static void registerAnUser(@Autowired TestRestTemplate restTemplate) {
        UserRegisterRequest user = new UserRegisterRequest("John", "Doe", "vcalomi@gmail.com", 32, "address", "john",
                "password");
        ResponseEntity<String> response = restTemplate.postForEntity(REGISTER_URI,
                user, String.class);
        token = response.getBody();
    }

    @Test
    void canCreateAProduct(){
        NewProductRequest newProduct = new NewProductRequest("product_1", 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity,
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void cantCreateAProductWithANullName(){
        NewProductRequest newProduct = new NewProductRequest(null, 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantCreateAProductWithAnEmptyName(){
        NewProductRequest newProduct = new NewProductRequest("", 20);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantCreateAProductWithLessThanOneOfStock(){
        NewProductRequest newProduct = new NewProductRequest("product_1", 0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        HttpEntity<NewProductRequest> requestEntity = new HttpEntity<>(newProduct, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(String.format("%s/create", PRODUCTS_URI), requestEntity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
