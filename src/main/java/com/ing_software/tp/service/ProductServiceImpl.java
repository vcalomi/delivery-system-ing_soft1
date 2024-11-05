package com.ing_software.tp.service;

import com.ing_software.tp.dto.EditProductRequest;
import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean validateStock(Long product_id, int quantityWanted){
        Optional<Product> product = this.findProductById(product_id);
        if (product.isPresent()) {
            int stock = product.get().getStock();
            return stock >= quantityWanted;
        }
        return false;
    }


    public Optional<Product> findProductById(Long product_id) {
        return productRepository.findById(product_id);
    }

    public Product createProduct(@Valid NewProductRequest productRequest) {
        Product product = new Product();
        product.setProduct_name(productRequest.getProduct_name());
        product.setStock(productRequest.getStock());
        product.setAttributes(productRequest.getAttributes());
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product editProductAttributes(EditProductRequest productRequest) throws Exception {
        Optional<Product> product = productRepository.findById(productRequest.getId());
        if (product.isPresent()) {

            Map<String, String> attributes = product.get().getAttributes();
            attributes.putAll(productRequest.getAttributes());
            product.get().setAttributes(attributes);

            return productRepository.save(product.get());
        }
        throw new Exception("Product not found");
    }
}
