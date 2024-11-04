package com.ing_software.tp.controller;

import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid NewProductRequest productRequest){
        Product product = productService.createProduct(productRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() throws Exception {
        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
