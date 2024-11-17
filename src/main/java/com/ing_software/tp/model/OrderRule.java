package com.ing_software.tp.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", length = 50)
public abstract class OrderRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract boolean isSatisfiedBy(Order order);
    public abstract String notSatisfiedMessage();
}
