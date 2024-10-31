package com.ing_software.tp.service;

import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.repository.ProductRepository;
import org.springframework.stereotype.Service;

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

    public Product createProduct(NewProductRequest productRequest) {
        Product product = new Product();
        product.setProduct_name(productRequest.getProduct_name());
        product.setStock(productRequest.getStock());
        return productRepository.save(product);
    }
}
