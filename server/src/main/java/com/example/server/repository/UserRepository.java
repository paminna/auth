package com.example.server.repository;

import com.example.server.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaBase<User, Integer> {

    Optional<User> findByUsername(String username);
}
