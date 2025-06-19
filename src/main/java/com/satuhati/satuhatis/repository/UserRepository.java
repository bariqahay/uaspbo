package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username); // <== Tambahin ini bro
}
