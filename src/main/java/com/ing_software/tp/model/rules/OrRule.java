package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class OrRule extends OrderRule {
    @OneToOne
    private OrderRule left;
    @OneToOne
    private OrderRule right;

    public OrRule(OrderRule left, OrderRule right) {
        this.left = left;
        this.right = right;
    }
    public boolean isSatisfiedBy(Order order) {
        return this.left.isSatisfiedBy(order) || this.right.isSatisfiedBy(order);
    }
}