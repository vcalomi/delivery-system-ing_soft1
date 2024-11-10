package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;
import lombok.Data;

@Data
public class AndRule implements OrderRule {
    private final OrderRule left;
    private final OrderRule right;

    public AndRule(OrderRule left, OrderRule right) {
        this.left = left;
        this.right = right;
    }

    public boolean isSatisfiedBy(Order order) {
        return this.left.isSatisfiedBy(order) && this.right.isSatisfiedBy(order);
    }
}

