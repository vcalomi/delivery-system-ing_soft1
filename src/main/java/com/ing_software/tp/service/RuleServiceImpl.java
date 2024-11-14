package com.ing_software.tp.service;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.*;
import com.ing_software.tp.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;

    public RuleServiceImpl(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public OrderRule createOrderRule(Map<String, Object> ruleRequest) {

        Map<String, Object> rule = (Map<String, Object>) ruleRequest.get("rule");
        String ruleType = (String) rule.get("ruleType");
        switch (ruleType) {
            case "minAttributeCount": {
                OrderRule minAttributeCountRule = new MinAttributeCount((String) rule.get("attribute"),
                        (String) rule.get("value"),
                        (String) rule.get("minValue"));
                return ruleRepository.save(minAttributeCountRule);
            }
            case "maxAttributeCount": {
                OrderRule maxAttributeCountRule = new MaxAttributeCount((String) rule.get("attribute"), (String) rule.get("value"),
                        (String) rule.get("maxValue"));
                return ruleRepository.save(maxAttributeCountRule);
            }
            case "maxAttributeSum": {
                OrderRule maxAttributeSum = new MaxAttributeSum((String) rule.get("attribute"),
                        (String) rule.get("maxValue"));
                return ruleRepository.save(maxAttributeSum);
            }
            case "minAttributeSum": {
                OrderRule minAttributeSum = new MinAttributeSum((String) rule.get("attribute"),
                        (String) rule.get("minValue"));
                return ruleRepository.save(minAttributeSum);
            }
            case "restrictedAttributeCombination": {
                OrderRule restrictedAttributeCombination = new RestrictedAttributeCombinationRule((String) rule.get(
                        "attribute"), (List<String>) rule.get("values"));
                return ruleRepository.save(restrictedAttributeCombination);
            }
            case "not": {
                OrderRule insideRule = createOrderRule(rule);
                OrderRule notRule = new NotRule(insideRule);
                return ruleRepository.save(notRule);
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
                return ruleRepository.save(andRule);
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
                return ruleRepository.save(orRule);
            }
        }
        return null;
    }

    public List<OrderRule> getAllRules() {
        return ruleRepository.findAll();
    }
}
