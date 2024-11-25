package com.ing_software.tp.controllerTests;

import com.ing_software.tp.dto.UserChangePasswordRequest;
import com.ing_software.tp.dto.UserForgetPasswordRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.repository.ProductRepository;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.EmailSenderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserPasswordEndpointTest {
    static String CHANGE_PASSWORD_URI = "/api/users/changePassword";
    static String REGISTER_URI = "/api/users/register";
    static String FORGET_PASSWORD_URI = "/api/users/forgetPassword";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    EmailSenderService emailSenderService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void resgisterUser() {
        User registeredUser = new User(null, "John", "Doe", "email@gmail.com", 20,
                "address", "John", "1234", "USER", null, "M");
        restTemplate.postForEntity(REGISTER_URI, registeredUser, String.class);
    }
    @AfterEach
    void deleteUsers(){
        userRepository.deleteAll();
    }

    @AfterAll
    static void cleanDatabase(@Autowired OrderProductRepository orderProductRepository,
                              @Autowired ProductRepository productRepository,
                              @Autowired UserRepository userRepository, @Autowired OrderRepository orderRepository) {
        productRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
        orderProductRepository.deleteAll();
    }

    @Test
    void canForgetPassword() {
        UserForgetPasswordRequest userForgetPasswordRequest = new UserForgetPasswordRequest("John");
        ResponseEntity<String> response = restTemplate.exchange(FORGET_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userForgetPasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cantForgetPasswordUsernameNotFound() {
        UserForgetPasswordRequest userForgetPasswordRequest = new UserForgetPasswordRequest("incorrectUsername");
        ResponseEntity<String> response = restTemplate.exchange(FORGET_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userForgetPasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantForgetPasswordMissingUsername() {
        UserForgetPasswordRequest userForgetPasswordRequest = new UserForgetPasswordRequest();
        ResponseEntity<String> response = restTemplate.exchange(FORGET_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userForgetPasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void canChangePassword() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("John",
                "1234", "12345","12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cantChangePasswordUsernameNotFound() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("NotFound",
                "1234", "12345","12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantChangePasswordOldPasswordIncorrect() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("John",
                "incorrect", "12345","12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantChangePasswordNewPasswordRepeatedDifferent() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest("John",
                "1234", "12345","different");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void cantChangePasswordWithMissingUsername() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        userChangePasswordRequest.setOldPassword("1234");
        userChangePasswordRequest.setNewPassword("12345");
        userChangePasswordRequest.setRepeatedNewPassword("12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantChangePasswordWithMissingOldPassword() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        userChangePasswordRequest.setUsername("John");
        userChangePasswordRequest.setNewPassword("12345");
        userChangePasswordRequest.setRepeatedNewPassword("12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantChangePasswordWithMissingNewPassword() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        userChangePasswordRequest.setUsername("John");
        userChangePasswordRequest.setOldPassword("1234");
        userChangePasswordRequest.setRepeatedNewPassword("12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void cantChangePasswordWithMissingRepeatedNewPassword() {
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        userChangePasswordRequest.setUsername("John");
        userChangePasswordRequest.setOldPassword("1234");
        userChangePasswordRequest.setNewPassword("12345");
        ResponseEntity<String> response = restTemplate.exchange(CHANGE_PASSWORD_URI, HttpMethod.PATCH,
                new HttpEntity<>(userChangePasswordRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
