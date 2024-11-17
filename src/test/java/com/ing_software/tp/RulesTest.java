package com.ing_software.tp;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderProduct;
import com.ing_software.tp.model.OrderStatus;
import com.ing_software.tp.model.User;
import com.ing_software.tp.model.rules.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RulesTest {

    @Test
    void emptyOrderShouldSatisfyMaxAttributeCountRule() {
        List<OrderProduct> products = new ArrayList<>();
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color", "red", "1");
        assertThat(rule.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void emptyOrderShouldSatisfyAndRule() {
        List<OrderProduct> products = new ArrayList<>();
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        AndRule rules = new AndRule(
                new MaxAttributeCount("color", "red", "1"),
                new MaxAttributeCount("color", "blue", "1")
        );
        assertThat(rules.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void emptyOrderShouldSatisfyOrRule() {
        List<OrderProduct> products = new ArrayList<>();
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        OrRule rules = new OrRule(
                new MaxAttributeCount("color", "red", "1"),
                new MaxAttributeCount("color", "blue", "1")
        );
        assertThat(rules.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void emptyOrderShouldSatisfyNotRule() {
        List<OrderProduct> products = new ArrayList<>();
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color", "blue", "1");
        NotRule notRule = new NotRule(rule);

        assertThat(notRule.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void cantHaveOrderWithMoreThanOneRedProduct(){
        Map<String,String> attributes = new HashMap<>();
        attributes.put("color","red");
        OrderProduct orderProduct = new OrderProduct(1L, 1L,"product_name1",1,attributes);
        OrderProduct product2 = new OrderProduct(2L, 2L,"product_name2",1,attributes);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        products.add(product2);
        Order order = new Order(1L,new User(),products,OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color","red","1");
        assertThat(rule.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void maxAttributeCountExactMatch() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "red");
        OrderProduct orderProduct = new OrderProduct(1L, 1L,"product_name1", 1, attributes);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color", "red", "1");
        assertThat(rule.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void maxAttributeCountExceedsLimit() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "red");
        OrderProduct product1 = new OrderProduct(1L, 1L, "product_name1", 1, attributes);
        OrderProduct product2 = new OrderProduct(2L, 2L, "product_name2", 1, attributes);
        List<OrderProduct> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color", "red", "1");
        assertThat(rule.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void maxAttributeCountWithNoMatchingAttribute() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "blue");
        OrderProduct orderProduct = new OrderProduct(1L, 1L, "product_name1", 1, attributes);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount rule = new MaxAttributeCount("color", "red", "1");
        assertThat(rule.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void twoRulesWithAndLogic(){
        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","red");
        attributes2.put("color","blue");
        OrderProduct orderProduct = new OrderProduct(1L, 1L,"product_name1",1,attributes);
        OrderProduct product2 = new OrderProduct(2L, 2L,"product_name2",1,attributes2);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        products.add(product2);
        products.add(product2);

        Order order = new Order(1L,new User(),products,OrderStatus.CREATED, LocalDateTime.now());
        AndRule rules = new AndRule(new MaxAttributeCount("color","red","1"),new MaxAttributeCount("color","blue","1"));
        assertThat(rules.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void twoRulesWithOrLogic(){
        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","green");
        attributes2.put("color","red");
        OrderProduct orderProduct = new OrderProduct(1L, 2L,"product_name1",1,attributes);
        OrderProduct product2 = new OrderProduct(2L, 2L,"product_name2",1,attributes2);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        products.add(product2);
        products.add(product2);

        Order order = new Order(1L,new User(),products,OrderStatus.CREATED, LocalDateTime.now());
        OrRule rules = new OrRule(new MaxAttributeCount("color","red","1"),new MaxAttributeCount("color","blue","1"));
        assertThat(rules.isSatisfiedBy(order)).isTrue();
    }

    @Test
    void notRuleTest() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "blue");
        OrderProduct orderProduct = new OrderProduct(1L, 1L, "product_name1", 1, attributes);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);

        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());
        MaxAttributeCount originalRule = new MaxAttributeCount("color", "blue", "1");
        NotRule notRule = new NotRule(originalRule);

        assertThat(notRule.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void notRuleWithAndCombinationTest() {
        Map<String, String> attributes1 = new HashMap<>();
        Map<String, String> attributes2 = new HashMap<>();
        attributes1.put("type", "liquid");
        attributes2.put("type", "gaseous");

        OrderProduct product1 = new OrderProduct(1L, 1L, "product1", 1, attributes1);
        OrderProduct product2 = new OrderProduct(2L, 2L, "product2", 1, attributes2);

        List<OrderProduct> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        MaxAttributeCount noLiquidRule = new MaxAttributeCount("type", "liquid", "0");
        MaxAttributeCount noGaseousRule = new MaxAttributeCount("type", "gaseous", "0");

        AndRule complexRule = new AndRule(
                new NotRule(noLiquidRule),
                new NotRule(noGaseousRule)
        );

        assertThat(complexRule.isSatisfiedBy(order)).isTrue();
    }
    @Test
    void notRuleWithOrCombinationTest() {
        Map<String, String> attributes1 = new HashMap<>();
        Map<String, String> attributes2 = new HashMap<>();
        attributes1.put("color", "green");
        attributes2.put("color", "blue");
        OrderProduct product1 = new OrderProduct(1L, 1L, "product_name1", 1, attributes1);
        OrderProduct product2 = new OrderProduct(1L, 1L, "product_name2", 1, attributes2);
        List<OrderProduct> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());
        NotRule notRule = new NotRule(
                new OrRule(
                        new MaxAttributeCount("color", "green", "1"),
                        new MaxAttributeCount("color", "blue", "0")
                )
        );
        assertThat(notRule.isSatisfiedBy(order)).isFalse();
    }

    @Test
    void restrictedCombinationRule(){
        Map<String,String> attributes = new HashMap<>();
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("color","green");
        attributes2.put("color","red");
        OrderProduct orderProduct = new OrderProduct(1L, 1L,"product_name1",1,attributes);
        OrderProduct product2 = new OrderProduct(2L, 2L,"product_name2",1,attributes2);
        List<OrderProduct> products = new ArrayList<>();
        products.add(orderProduct);
        products.add(product2);
        List<String> restrictions = List.of("red","green");

        Order order = new Order(1L,new User(),products,OrderStatus.CREATED, LocalDateTime.now());
        RestrictedAttributeCombinationRule rule = new RestrictedAttributeCombinationRule("color",restrictions);

        assertThat(rule.isSatisfiedBy(order)).isFalse();
    }
    @Test
    void restrictedCombinationRuleWithMultipleRestrictions() {
        Map<String, String> attributesRed = new HashMap<>();
        attributesRed.put("color", "red");
        attributesRed.put("size", "1");

        Map<String, String> attributesGreen = new HashMap<>();
        attributesGreen.put("color", "green");
        attributesGreen.put("size", "2");

        OrderProduct productRed = new OrderProduct(1L, 1L, "product_red", 1, attributesRed);
        OrderProduct productGreen = new OrderProduct(2L, 2L, "product_green", 1, attributesGreen);

        List<OrderProduct> products = new ArrayList<>();
        products.add(productRed);
        products.add(productGreen);

        List<String> restrictedColorCombination = List.of("red", "green");
        List<String> restrictedSizeCombination = List.of("1", "2");

        RestrictedAttributeCombinationRule restrictedColorRule = new RestrictedAttributeCombinationRule("color", restrictedColorCombination);
        RestrictedAttributeCombinationRule restrictedSizeRule = new RestrictedAttributeCombinationRule("size", restrictedSizeCombination);

        AndRule combinedRule = new AndRule(restrictedColorRule, restrictedSizeRule);

        Order order = new Order(1L, new User(), products, OrderStatus.CREATED, LocalDateTime.now());

        assertThat(combinedRule.isSatisfiedBy(order)).isFalse();
    }

}
