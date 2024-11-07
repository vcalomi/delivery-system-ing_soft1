package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;

public class MaxAttributeCount implements OrderRule {

    private final String attribute;
    private final String value;
    private final int maxCount;

    public MaxAttributeCount(String attribute, String value, String maxCount) {
        this.attribute = attribute;
        this.value = value;
        this.maxCount = Integer.parseInt(maxCount);
    }
    public boolean isSatisfiedBy(Order order) {
        long count = order.getProducts().stream().filter(product -> product.hasAttribute(attribute, value))
                .count();
        return count <= maxCount;

    }
}