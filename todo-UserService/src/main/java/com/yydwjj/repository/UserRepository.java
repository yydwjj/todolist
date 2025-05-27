package com.yydwjj.repository;

import com.yydwjj.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    List<User> findByRole(String role);

    boolean existsByName(String name);
}