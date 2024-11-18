package com.ing_software.tp.repository;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByOwner(User owner);
}
