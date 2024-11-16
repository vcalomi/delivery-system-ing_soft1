package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.MinAttributeSum;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MinAttributeSumFactory implements RuleFactory {

    public OrderRule create(Map<String, Object> ruleData) {
        return new MinAttributeSum(
                (String) ruleData.get("attribute"),
                (String) ruleData.get("minSum")
        );
    }
}
