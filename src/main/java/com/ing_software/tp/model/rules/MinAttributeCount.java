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
public class MinAttributeCount extends OrderRule {

    private String attribute;
    private String attributeValue;
    private int minCount;

    public MinAttributeCount(String attribute, String attributeValue, String minCount) {
        this.attribute = attribute;
        this.attributeValue = attributeValue;
        this.minCount = Integer.parseInt(minCount);
    }
    public boolean isSatisfiedBy(Order order) {
        int count = 0;
        for (OrderProduct orderProduct: order.getProducts()) {
            if (orderProduct.hasAttribute(attribute, attributeValue)) {
                count += orderProduct.getQuantity();
            }
        }
        return count >= minCount;

    }

    public String notSatisfiedMessage() {
        return String.format("There must be a min of %d products with %s equal to %s", this.minCount,
                this.attribute, this.attributeValue);
    }
}

