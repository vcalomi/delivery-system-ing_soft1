package com.ing_software.tp;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.Product;
import com.ing_software.tp.model.User;
import com.ing_software.tp.model.rules.AndRule;
import com.ing_software.tp.model.rules.MaxAttributeCount;
import com.ing_software.tp.model.rules.OrRule;
import com.ing_software.tp.model.rules.RestrictedAttributeCombinationRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RulesTest {

    @Test
    void cantHaveOrderWithMoreThanOneRedProduct(){
        Map<String,String> attributes = new HashMap<>();
        attributes.put("color","red");
        Product product = new Product(1L,"product_name1",1,attributes);
        Product product2 = new Product(2L,"product_name2",1,attributes);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Order order = new Order(1L,new User(),products,true);

        MaxAttributeCount rule = new MaxAttributeCount("color","red","1");
        assertThat(rule.isSatisfiedBy(order)).isFalse();

    }

    @Test
    void twoRulesWithAndLogic(){
        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","red");
        attributes2.put("color","blue");
        Product product = new Product(1L,"product_name1",1,attributes);
        Product product2 = new Product(2L,"product_name2",1,attributes2);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        products.add(product2);


        Order order = new Order(1L,new User(),products,true);
        AndRule rules = new AndRule(new MaxAttributeCount("color","red","1"),new MaxAttributeCount("color","blue","1"));
        assertThat(rules.isSatisfiedBy(order)).isFalse();

    }

    @Test
    void twoRulesWithOrLogic(){
        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","green");
        attributes2.put("color","red");
        Product product = new Product(1L,"product_name1",1,attributes);
        Product product2 = new Product(2L,"product_name2",1,attributes2);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        products.add(product2);


        Order order = new Order(1L,new User(),products,true);
        OrRule rules = new OrRule(new MaxAttributeCount("color","red","1"),new MaxAttributeCount("color","blue","1"));
        assertThat(rules.isSatisfiedBy(order)).isTrue();

    }

    @Test
    void restrictedCombinationRule(){

        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","green");
        attributes2.put("color","red");
        Product product = new Product(1L,"product_name1",1,attributes);
        Product product2 = new Product(2L,"product_name2",1,attributes2);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        List<String> restrictions = List.of("red","green");

        Order order = new Order(1L,new User(),products,true);
        RestrictedAttributeCombinationRule rule = new RestrictedAttributeCombinationRule("color",restrictions);

        assertThat(rule.isSatisfiedBy(order)).isFalse();

    }
}
