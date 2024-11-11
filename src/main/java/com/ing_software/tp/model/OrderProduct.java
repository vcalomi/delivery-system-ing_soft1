package com.ing_software.tp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long product_id;
    private String product_name;
    private int quantity;
    @ElementCollection
    private Map<String, String> attributes;

    public boolean hasAttribute(String attributeType,String value){
        return attributes.containsKey(attributeType) && attributes.get(attributeType).equals(value);
    }

    public String getAttribute(String attributeType) {

        return attributes.get(attributeType);

    }
}
