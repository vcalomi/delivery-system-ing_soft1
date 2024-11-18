package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.AndRule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AndRuleFactory implements RuleFactory {
    private final ObjectProvider<RuleFactoryRegistry> factoryRegistryProvider;

    public AndRuleFactory(ObjectProvider<RuleFactoryRegistry> factoryRegistryProvider) {
        this.factoryRegistryProvider = factoryRegistryProvider;
    }

    @Override
    public OrderRule create(Map<String, Object> ruleData) {
        List<Map<String, Object>> rulesData = (List<Map<String, Object>>) ruleData.get("rules");
        return createNestedRules(rulesData);
    }

    private OrderRule createNestedRules(List<Map<String, Object>> rulesData) {
        if (rulesData.size() == 1) {
            return createRuleFromData(rulesData.getFirst());
        } else {
            OrderRule leftRule = createRuleFromData(rulesData.getFirst());
            List<Map<String, Object>> remainingRules = rulesData.subList(1, rulesData.size());
            OrderRule rightRule = createNestedRules(remainingRules);
            return new AndRule(leftRule, rightRule);
        }
    }

    private OrderRule createRuleFromData(Map<String, Object> ruleData) {
        Map<String, Object> innerRuleData = (Map<String, Object>) ruleData.get("rule");

        String ruleType = (String) innerRuleData.get("ruleType");

        RuleFactoryRegistry factoryRegistry = factoryRegistryProvider.getObject();
        RuleFactory factory = factoryRegistry.getFactory(ruleType);

        if (factory == null) {
            throw new IllegalArgumentException("Unsupported rule type: " + ruleType);
        }

        return factory.create(innerRuleData);
    }
}
