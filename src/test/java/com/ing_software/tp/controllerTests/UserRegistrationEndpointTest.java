package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.UserRegisterRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.EmailSenderService;
import com.ing_software.tp.service.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationEndpointTest {

    private static final String REGISTER_URI = "/api/users/register";

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    JwtService jwtService;

    @MockBean
    EmailSenderService emailSenderService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void deleteUsers(){
        userRepository.deleteAll();
    }

    @Test
    void canRegisterUser(){
        UserRegisterRequest user = new UserRegisterRequest("John", "Doe", "vcalomi@gmail.com", 32, "address", "john",
                "password", "M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void aSuccessfulRegistrationReturnsAToken(){

        User user = new User(null, "John", "Doe", "email@gmail.com", 32, "address", "john", "password", "USER", null,
                "M");

        ResponseEntity<String> response = restTemplate.postForEntity(REGISTER_URI, user, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotBlank();
        assertThat(response.getBody()).isInstanceOf(String.class);
    }

    @Test
    void cantRegisterWithMissingName(){
        User user = new User();
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        user.setAge(32);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingLastname(){
        User user = new User();
        user.setName("John");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        user.setAge(32);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingEmail(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setAddress("address");
        user.setAge(32);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingAge(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingAddress(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithMissingUsername(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithMissingPassword(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setUsername("johndoe");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithEmptyName(){
        User user = new User();
        user.setName("");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithEmptyLastname(){
        User user = new User();
        user.setName("John");
        user.setLastname("");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithEmptyEmail(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithEmptyAddress(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithAgeLessThan18(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(12);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithAgeMoreThan100(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(120);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithEmptyUsername(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("");
        user.setPassword("password");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithEmptyPassword(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("");
        user.setGender("M");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithEmptyGender(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setGender("");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantRegisterWithMissingGender(){
        User user = new User();
        user.setName("John");
        user.setLastname("Doe");
        user.setEmail("email@gmail.com");
        user.setAge(32);
        user.setAddress("address");
        user.setUsername("johndoe");
        user.setPassword("password");
        ResponseEntity<?> response = restTemplate.postForEntity(REGISTER_URI, user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void registeringReturnsACorrectJWT() {
        String uniqueUsername = String.format("john%d", System.currentTimeMillis());
        User user = new User(null, "John", "Doe", "email@gmail.com", 32, "address", uniqueUsername, "password", "USER", null, "M");
        ResponseEntity<String> response = restTemplate.postForEntity(REGISTER_URI, user,String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(jwtService.validateToken(response.getBody(), user)).isTrue();

    }

    @Test
    void usernameCanBeExtractedFromJWT() {
        User user = new User(null, "John", "Doe", "email@gmail.com", 32, "address", "john", "password", "USER", null,
                "M");
        ResponseEntity<String> response = restTemplate.postForEntity(REGISTER_URI, user,String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(jwtService.validateToken(response.getBody(), user)).isTrue();
        Assertions.assertThat(jwtService.extractUsername(response.getBody())).isEqualTo(user.getUsername());
    }

}
