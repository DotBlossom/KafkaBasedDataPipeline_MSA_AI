package com.example.kafkaAsyncTest.repository;

import com.example.kafkaAsyncTest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}