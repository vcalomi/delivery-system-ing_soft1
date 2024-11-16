package com.ing_software.tp.model.factory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleFactoryRegistry {
    private final Map<String, RuleFactory> factories = new HashMap<>();

    public RuleFactoryRegistry(List<RuleFactory> factoryList) {
        for (RuleFactory factory : factoryList) {
            String ruleType = factory.getClass().getSimpleName().replace("Factory", "").toLowerCase();
            factories.put(ruleType, factory);
        }
    }

    public RuleFactory getFactory(String ruleType) {
        return factories.get(ruleType);
    }
}
