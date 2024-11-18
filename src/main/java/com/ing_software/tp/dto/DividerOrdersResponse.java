package com.ing_software.tp.dto;

import com.ing_software.tp.model.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DividerOrdersResponse {
    List<List<OrderProduct>> dividedProducts;

}
