package com.ing_software.tp.service;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleService {

    public OrderRule createOrderRule(Map<String, Object> ruleRequest) {

        Map<String, Object> rule = (Map<String, Object>) ruleRequest.get("rule");
        String ruleType = (String) rule.get("ruleType");
        System.out.println(rule);
        switch (ruleType) {
            case "minAttributeCount": {
                OrderRule minAttributeCountRule = new MinAttributeCount((String) rule.get("attribute"),
                        (String) rule.get("value"),
                        (String) rule.get("minValue"));
                return minAttributeCountRule;
            }
            case "maxAttributeCount": {
                OrderRule maxAttributeCountRule = new MaxAttributeCount((String) rule.get("attribute"), (String) rule.get("value"),
                        (String) rule.get("maxValue"));
                return maxAttributeCountRule;
            }
            case "maxAttributeSum": {
                OrderRule maxAttributeSum = new MaxAttributeSum((String) rule.get("attribute"), (String) rule.get(
                        "value"),
                        (String) rule.get("maxValue"));
                return maxAttributeSum;
            }
            case "minAttributeSum": {
                OrderRule minAttributeSum = new MinAttributeSum((String) rule.get("attribute"), (String) rule.get(
                        "value"),
                        (String) rule.get("minValue"));
                return minAttributeSum;
            }
            case "restrictedAttributeCombination": {
                OrderRule restrictedAttributeCombination = new RestrictedAttributeCombinationRule((String) rule.get(
                        "attribute"), (List<String>) rule.get("values"));
                return restrictedAttributeCombination;
            }
            case "not": {
                OrderRule insideRule = createOrderRule(rule);
                OrderRule notRule = new NotRule(insideRule);
                return notRule;
            }
            case "and": {
                List<Map<String, Object>> rules = (List<Map<String, Object>>) rule.get("rules");
                if (rules.size() == 1) {
                    return createOrderRule(rules.removeFirst());
                }
                OrderRule leftRule = createOrderRule(rules.removeFirst());
                rule.put("rules", rules);
                ruleRequest.put("rule", rule);
                OrderRule rightRule = createOrderRule(ruleRequest);
                OrderRule andRule = new AndRule(leftRule, rightRule);
                return andRule;
            }
            case "or": {
                List<Map<String, Object>> rules = (List<Map<String, Object>>) rule.get("rules");
                if (rules.size() == 1) {
                    return createOrderRule(rules.removeFirst());
                }
                OrderRule leftRule = createOrderRule(rules.removeFirst());
                rule.put("rules", rules);
                ruleRequest.put("rule", rule);
                OrderRule rightRule = createOrderRule(ruleRequest);
                OrderRule orRule = new OrRule(leftRule, rightRule);
                return orRule;
            }
        }
        return null;
    }
}
