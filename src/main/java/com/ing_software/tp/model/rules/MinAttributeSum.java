package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;

public class MinAttributeSum implements OrderRule {

    private final String attribute;
    private final String value;
    private final int minSum;

    public MinAttributeSum(String attribute, String value, String maxSum) {
        this.attribute = attribute;
        this.value = value;
        this.minSum = Integer.parseInt(maxSum);
    }
    public boolean isSatisfiedBy(Order order) {
        Double sum = order.getProducts().stream()
                .mapToDouble(product -> Double.parseDouble(product.getAttribute(attribute)))
                .sum();

        return sum >= minSum;

    }
}
