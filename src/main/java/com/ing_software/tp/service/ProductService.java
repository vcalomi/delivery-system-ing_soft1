package com.ing_software.tp.service;

import com.ing_software.tp.dto.EditProductRequest;
import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.model.Product;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    boolean validateStock(Long product_id, int quantityWanted);

    Optional<Product> findProductById(Long product_id);

    Product createProduct(NewProductRequest productRequest);

    List<Product> getProducts() throws Exception;

    Product editProductAttributes(EditProductRequest productRequest) throws Exception;
}
