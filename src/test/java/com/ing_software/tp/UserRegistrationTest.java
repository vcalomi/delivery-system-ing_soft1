package com.ing_software.tp;

import com.ing_software.tp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void canRegisterUser(){
        User user = new User("Valentin", "Calomino", "email@gmail.com", 22, "address");
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void cantRegisterWithMissingName(){
        User user = new User();
        user.setLastname("Calomino");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        user.setAge(22);
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingLastname(){
        User user = new User();
        user.setName("Valentin");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        user.setAge(22);
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingEmail(){
        User user = new User();
        user.setName("Valentin");
        user.setLastname("Calomino");
        user.setAddress("address");
        user.setAge(22);
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingAge(){
        User user = new User();
        user.setName("Valentin");
        user.setLastname("Calomino");
        user.setEmail("email@gmail.com");
        user.setAddress("address");
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void cantRegisterWithMissingAddress(){
        User user = new User();
        user.setName("Valentin");
        user.setLastname("Calomino");
        user.setEmail("email@gmail.com");
        user.setAge(22);
        ResponseEntity<?> response = restTemplate.postForEntity("/register", user,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
