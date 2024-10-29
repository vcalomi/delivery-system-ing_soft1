package com.ing_software.tp;

import com.ing_software.tp.dto.LoginResponse;
import com.ing_software.tp.dto.UserLoginRequest;
import com.ing_software.tp.model.User;
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
public class UserLoginControllerTest {

    private static final String LOGIN_URL = "/users/login";

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void cantLoginWithNonExistentUserCredentials(){
        ResponseEntity<?> response = restTemplate.postForEntity(LOGIN_URL, new UserLoginRequest(), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginWithCorrectCredentialsReturnsCorrectLoginResponse(){
        User user = new User(null, "John", "Doe", "email@email.com", 20, "address", "John", "1234");
        restTemplate.postForEntity("/users/register", user, Void.class);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(LOGIN_URL, new UserLoginRequest("John", "1234"),
                LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(user.getName());
        assertThat(response.getBody().getLastname()).isEqualTo(user.getLastname());
        assertThat(response.getBody().getToken()).isNotBlank();
    }
}
