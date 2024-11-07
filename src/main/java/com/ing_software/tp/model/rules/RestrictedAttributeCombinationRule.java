package com.ing_software.tp.model.rules;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RestrictedAttributeCombinationRule implements OrderRule {
    private final String attribute;
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

}
