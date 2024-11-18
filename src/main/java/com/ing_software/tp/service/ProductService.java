package com.ing_software.tp.service;

import com.ing_software.tp.dto.EditProductRequest;
import com.ing_software.tp.dto.IncrementStockRequest;
import com.ing_software.tp.dto.NewProductRequest;
import com.ing_software.tp.model.OrderProduct;
import com.ing_software.tp.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    boolean validateStock(Long product_id, int quantityWanted);

    Optional<Product> findProductById(Long product_id);

    Product createProduct(NewProductRequest productRequest);

    List<Product> getProducts() throws Exception;

    Product editProductAttributes(EditProductRequest productRequest) throws Exception;

    void updateStock(List<OrderProduct> products);

    void restoreStock(List<OrderProduct> products);

    Product incrementStock(Long productId, IncrementStockRequest incrementStockRequest) throws Exception;
}
