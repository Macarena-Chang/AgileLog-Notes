package com.macarenachang.authservice.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.macarenachang.authservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
