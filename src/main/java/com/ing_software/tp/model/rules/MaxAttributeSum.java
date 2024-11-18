package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class MaxAttributeSum extends OrderRule {

    private String attribute;
    private int maxSum;

    public MaxAttributeSum(String attribute, String maxSum) {
        this.attribute = attribute;
        this.maxSum = Integer.parseInt(maxSum);
    }
    public boolean isSatisfiedBy(Order order) {
        Double sum = order.getProducts().stream()
                .mapToDouble(product -> Double.parseDouble(product.getAttribute(attribute)))
                .sum();

        return sum <= maxSum;

    }

    public String notSatisfiedMessage() {
        return String.format("There must be a max %s value of %d", this.attribute, this.maxSum);
    }
}

