package com.ing_software.tp.repository;

import com.ing_software.tp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    UserDetails findByUsername(String username);
}
