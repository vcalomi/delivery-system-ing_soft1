package com.ing_software.tp.controller;

import com.ing_software.tp.dto.EditProductRequest;
import com.ing_software.tp.dto.IncrementStockRequest;
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

    @PatchMapping("/edit")
    public ResponseEntity<Product> editProduct(@RequestBody @Valid EditProductRequest productRequest) throws Exception {
        Product product = productService.editProductAttributes(productRequest);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() throws Exception {
        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PatchMapping("/incrementStock/{productId}")
    public ResponseEntity<Product> incrementStock(@PathVariable Long productId, @RequestBody @Valid IncrementStockRequest incrementStockRequest) throws Exception {
        Product product = productService.incrementStock(productId, incrementStockRequest);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
