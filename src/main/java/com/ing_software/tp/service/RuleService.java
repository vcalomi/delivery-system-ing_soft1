package com.ing_software.tp.service;

import com.ing_software.tp.model.OrderRule;

import java.util.List;
import java.util.Map;

public interface RuleService {
    OrderRule createOrderRule(Map<String, Object> ruleRequest);
    List<OrderRule> getAllRules();
}
