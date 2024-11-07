package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;

public class NotRule implements OrderRule {
    private final OrderRule rule;

    public NotRule(OrderRule rule) {
        this.rule = rule;
    }
    public boolean isSatisfiedBy(Order order) {
        return !this.rule.isSatisfiedBy(order);
    }
}
