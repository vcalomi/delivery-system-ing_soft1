package com.ing_software.tp.service;

import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.model.rules.AndRule;
import com.ing_software.tp.model.rules.MaxAttributeCount;
import com.ing_software.tp.model.rules.NotRule;
import com.ing_software.tp.service.factory.RuleType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleService {


    private final Map<String, RuleType> rulesTypes = new HashMap<>();

    public RuleService() {
        this.rulesTypes.put("and", RuleType.AND_RULE);
        this.rulesTypes.put("or", RuleType.OR_RULE);
        this.rulesTypes.put("not", RuleType.NOT_RULE);
    }

    public OrderRule createOrderRule(Map<String, Object> ruleRequest) {

        // not: {}
        // and: left & right -> {} & {}
        // or: left & right -> {} & {}
        Map<String, Object> rule = (Map<String, Object>) ruleRequest.get("rule");
        String ruleType = (String) rule.get("ruleType");
        System.out.println(rule);
        switch (ruleType) {
            case "maxAttributeCount":
                OrderRule maxAttributeCountRule = new MaxAttributeCount((String) rule.get("attribute"), (String) rule.get("value"),
                        (String) rule.get("maxValue"));
                return maxAttributeCountRule;
            case "not":
                OrderRule insideRule = createOrderRule(rule);
                OrderRule notRule = new NotRule(insideRule);
                return notRule;
            case "and":
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
        return null;
    }
}
