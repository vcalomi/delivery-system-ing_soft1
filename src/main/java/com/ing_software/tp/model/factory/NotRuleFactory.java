package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.NotRule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotRuleFactory implements RuleFactory {
    private final ObjectProvider<RuleFactoryRegistry> factoryRegistryProvider;

    public NotRuleFactory(ObjectProvider<RuleFactoryRegistry> factoryRegistryProvider) {
        this.factoryRegistryProvider = factoryRegistryProvider;
    }

    @Override
    public OrderRule create(Map<String, Object> ruleData) {
        Map<String, Object> insideRuleData = (Map<String, Object>) ruleData.get("rule");
        RuleFactoryRegistry factoryRegistry = factoryRegistryProvider.getObject();
        RuleFactory factory = factoryRegistry.getFactory((String) insideRuleData.get("ruleType"));
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported rule type: " + insideRuleData.get("ruleType"));
        }
        OrderRule insideRule = factory.create(insideRuleData);
        return new NotRule(insideRule);
    }
}

