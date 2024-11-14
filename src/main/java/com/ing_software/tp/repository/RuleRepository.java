package com.ing_software.tp.repository;

import com.ing_software.tp.model.OrderRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<OrderRule, Long> {

}
