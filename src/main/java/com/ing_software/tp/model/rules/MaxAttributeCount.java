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
    private String attributeValue;
    private int maxCount;

    public MaxAttributeCount(String attribute, String attributeValue, String maxCount) {
        this.attribute = attribute;
        this.attributeValue = attributeValue;
        this.maxCount = Integer.parseInt(maxCount);
    }
    public boolean isSatisfiedBy(Order order) {
        int count = 0;
        for (OrderProduct orderProduct: order.getProducts()) {
            if (orderProduct.hasAttribute(attribute, attributeValue)) {
                count += orderProduct.getQuantity();
            }
        }
        return count <= maxCount;

    }
}