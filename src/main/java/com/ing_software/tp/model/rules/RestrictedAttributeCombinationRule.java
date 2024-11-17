package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class RestrictedAttributeCombinationRule extends OrderRule {
    private String attribute;
    private List<String> restrictedCombinations;

    public RestrictedAttributeCombinationRule(String attribute, List<String> values){

        this.attribute = attribute;
        restrictedCombinations = values;

    }
    public boolean isSatisfiedBy(Order order) {

        Set<Object> attributeValues = order.getProducts().stream()
                .map(product -> product.getAttribute(attribute))
                .collect(Collectors.toSet());

        return !attributeValues.containsAll(restrictedCombinations);
    }

    public String notSatisfiedMessage() {
        return String.format("The following %s values can't be combined %s", this.attribute,
                this.restrictedCombinations.toString());
    }

}
