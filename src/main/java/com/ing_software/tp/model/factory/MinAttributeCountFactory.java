package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.MinAttributeCount;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MinAttributeCountFactory implements RuleFactory {

    public OrderRule create(Map<String, Object> ruleData) {
        return new MinAttributeCount(
                (String) ruleData.get("attribute"),
                (String) ruleData.get("value"),
                (String) ruleData.get("minValue")
        );
    }
}

