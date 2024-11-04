package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginEndpointTest {

    private static final String LOGIN_URI = "/api/users/login";
    private static final String REGISTER_URI = "/api/users/register";

    @Autowired
    TestRestTemplate restTemplate;

    static User registeredUser = null;

    @BeforeAll
    static void registerUser(@Autowired TestRestTemplate restTemplate){
        registeredUser = new User(null, "John", "Doe", "email@email.com", 20, "address", "John1", "1234", "ADMIN");
        restTemplate.postForEntity(REGISTER_URI, registeredUser, Void.class);
    }

    @Test
    void cantLoginWithNonExistentUserCredentials(){
        ResponseEntity<?> response = restTemplate.postForEntity(LOGIN_URI, new UserLoginRequest(), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginWithCorrectCredentialsReturnsCorrectLoginResponse(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("John1", "1234");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(registeredUser.getName());
        assertThat(response.getBody().getLastname()).isEqualTo(registeredUser.getLastname());
        assertThat(response.getBody().getToken()).isNotBlank();
    }
    @Test
    void cantLoginWithIncorrectUsername(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("incorrectUsername", "1234");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantLoginWithIncorrectPassword(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("John1", "incorrectPassword");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantLoginWithMissingUsername(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword("1234");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantLoginWithMissingPassword(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("John1");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
