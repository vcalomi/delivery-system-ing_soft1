package com.ing_software.tp.model.factory;

import com.ing_software.tp.model.OrderRule;

import java.util.Map;

public interface RuleFactory {
    OrderRule create(Map<String, Object> ruleData);
}
