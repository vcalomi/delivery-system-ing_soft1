package com.ing_software.tp.service;

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
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            int stock = product.get().getStock();
            return stock >= quantityWanted;
        }
        return false;
    }
}
