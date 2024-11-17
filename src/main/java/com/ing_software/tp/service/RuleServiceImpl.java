package com.ing_software.tp.service;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.factory.RuleFactory;
import com.ing_software.tp.model.factory.RuleFactoryRegistry;
import com.ing_software.tp.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final RuleFactoryRegistry factoryRegistry;

    public RuleServiceImpl(RuleRepository ruleRepository, RuleFactoryRegistry factoryRegistry) {
        this.ruleRepository = ruleRepository;
        this.factoryRegistry = factoryRegistry;
    }

    public OrderRule createOrderRule(Map<String, Object> ruleRequest) {
        Map<String, Object> rule = (Map<String, Object>) ruleRequest.get("rule");
        String ruleType = (String) rule.get("ruleType");
        RuleFactory factory = factoryRegistry.getFactory(ruleType);

        if (factory == null) {
            throw new IllegalArgumentException(String.format("Unsupported rule type: %s", ruleType));
        }

        OrderRule orderRule = factory.create(rule);
        return ruleRepository.save(orderRule);
    }

    public List<OrderRule> getAllRules() {
        return ruleRepository.findAll();
    }
}
