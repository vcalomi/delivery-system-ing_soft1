package com.ing_software.tp.service;

import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.model.Product;

import java.util.Optional;

public interface ProductService {

    boolean validateStock(Long product_id, int quantityWanted);

    Optional<Product> findProductById(Long product_id);
}
