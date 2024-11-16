package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class NotRule extends OrderRule {
    @OneToOne(cascade = CascadeType.ALL)
    private OrderRule rule;

    public NotRule(OrderRule rule) {
        this.rule = rule;
    }
    public boolean isSatisfiedBy(Order order) {
        return !this.rule.isSatisfiedBy(order);
    }

    public String notSatisfiedMessage() {
        String message = this.rule.notSatisfiedMessage();
        return String.format("!%s", message);
    }
}
