package com.example.demologin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demologin.model.User;

public interface UserRepository  extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
}
