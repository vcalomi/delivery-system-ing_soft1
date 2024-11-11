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
public class MaxAttributeCount extends OrderRule {

    private String attribute;
    private String value;
    private int maxCount;

    public MaxAttributeCount(String attribute, String value, String maxCount) {
        this.attribute = attribute;
        this.value = value;
        this.maxCount = Integer.parseInt(maxCount);
    }
    public boolean isSatisfiedBy(Order order) {
        int count = 0;
        for (OrderProduct orderProduct: order.getProducts()) {
            if (orderProduct.hasAttribute(attribute, value)) {
                count += orderProduct.getQuantity();
            }
        }
        return count <= maxCount;

    }
}