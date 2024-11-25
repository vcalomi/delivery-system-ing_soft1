package com.ing_software.tp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.model.User;
import com.ing_software.tp.repository.UserRepository;
import com.ing_software.tp.service.ProductService;
import com.ing_software.tp.service.RuleServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader {
    private ObjectMapper mapper;
    private List<Map<String,Object>> rules;
    private RuleServiceImpl ruleService;
    private List<NewProductRequest> products;
    private ProductService productService;
    private UserRepository userRepository;
    private User user;
    private PasswordEncoder passwordEncoder;

    public DataLoader(RuleServiceImpl ruleService, ProductService productService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.ruleService = ruleService;
        this.productService = productService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = new ObjectMapper();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadRules(){
        try(InputStream inputStream = getClass().getResourceAsStream("/rules.json")){
            rules = mapper.readValue(inputStream, new TypeReference<List<Map<String,Object>>>(){});
            for (Map<String, Object> rule: rules) {
                ruleService.createOrderRule(rule);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadProducts(){
        try(InputStream inputStream = getClass().getResourceAsStream("/products.json")){
            products = mapper.readValue(inputStream, new TypeReference<List<NewProductRequest>>(){});
            for (NewProductRequest request: products) {
                productService.createProduct(request);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadAdmin(){
        try(InputStream inputStream = getClass().getResourceAsStream("/admin_user.json")){
            user = mapper.readValue(inputStream, new TypeReference<User>(){});
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
