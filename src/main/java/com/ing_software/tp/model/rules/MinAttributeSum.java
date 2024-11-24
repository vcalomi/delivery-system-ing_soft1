package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderProduct;
import com.ing_software.tp.model.OrderRule;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class MinAttributeSum extends OrderRule {

    private String attribute;
    private int minSum;

    public MinAttributeSum(String attribute, String maxSum) {
        this.attribute = attribute;
        this.minSum = Integer.parseInt(maxSum);
    }
    public boolean isSatisfiedBy(Order order) {
        double sum = 0;
        for (OrderProduct orderProduct: order.getProducts()) {
            if (orderProduct.getAttribute(attribute) == null) {
                continue;
            }
            sum += Double.parseDouble(orderProduct.getAttributes().get(attribute)) * orderProduct.getQuantity();
        }

        return sum >= minSum;

    }

    public String notSatisfiedMessage() {
        return String.format("There must be a min %s value of %d", this.attribute, this.minSum);
    }
}
