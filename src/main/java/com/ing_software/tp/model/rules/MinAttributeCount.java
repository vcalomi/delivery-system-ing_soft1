package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;

public class MinAttributeCount implements OrderRule {

    private final String attribute;
    private final String value;
    private final int minCount;

    public MinAttributeCount(String attribute, String value, String minCount) {
        this.attribute = attribute;
        this.value = value;
        this.minCount = Integer.parseInt(minCount);
    }
    public boolean isSatisfiedBy(Order order) {
        long count = order.getProducts().stream().filter(product -> product.hasAttribute(attribute, value))
                .count();
        return count >= minCount;

    }
}

