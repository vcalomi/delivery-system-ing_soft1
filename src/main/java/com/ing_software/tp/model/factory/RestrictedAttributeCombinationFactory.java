package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.RestrictedAttributeCombinationRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RestrictedAttributeCombinationFactory implements RuleFactory {

    public OrderRule create(Map<String, Object> ruleData) {
        return new RestrictedAttributeCombinationRule(
                (String) ruleData.get("attribute"),
                (List<String>) ruleData.get("values")
        );
    }
}
