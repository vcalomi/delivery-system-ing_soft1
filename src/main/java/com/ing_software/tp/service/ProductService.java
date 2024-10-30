package com.ing_software.tp.service;

public interface ProductService {

    boolean validateStock(Long product_id, int quantityWanted);
}
