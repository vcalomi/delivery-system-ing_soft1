package com.ing_software.tp.config;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing_software.tp.model.OrderRule;
import com.ing_software.tp.service.RuleService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader {
    private ObjectMapper mapper;
    private Map<String,Object> rules;
    private RuleService ruleService;

    public DataLoader(RuleService ruleService) {
        this.ruleService = ruleService;
        this.mapper = new ObjectMapper();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){
        try(InputStream inputStream = getClass().getResourceAsStream("/rules.json")){
            rules = mapper.readValue(inputStream, new TypeReference<Map<String,Object>>(){});
            OrderRule rule = ruleService.createOrderRule(rules);
            System.out.println(rule);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }

}
