package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginEndpointTest {

    private static final String LOGIN_URI = "/api/users/login";
    private static final String REGISTER_URI = "/api/users/register";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    static User registeredUser = null;

    @BeforeAll
    static void registerUser(@Autowired TestRestTemplate restTemplate) {

        registeredUser = new User(null, "John", "Doe", "email@email.com", 20,
                "address", "John1", "1234", "USER", null, "M");
        restTemplate.postForEntity(REGISTER_URI, registeredUser, String.class);
    }

    @Test
    void cantLoginWithNonExistentUserCredentials(){
        ResponseEntity<?> response = restTemplate.postForEntity(LOGIN_URI, new UserLoginRequest(), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginWithCorrectCredentialsReturnsAValidToken(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("John1", "1234");
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        UserDetails user = userRepository.findByUsername(userLoginRequest.getUsername());
        assertThat(jwtService.validateToken(response.getBody(), user)).isTrue();
    }
    @Test
    void cantLoginWithIncorrectUsername(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("incorrectUsername", "1234");
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantLoginWithIncorrectPassword(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("John1", "incorrectPassword");
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantLoginWithMissingUsername(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword("1234");
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantLoginWithMissingPassword(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("John1");
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URI, userLoginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
