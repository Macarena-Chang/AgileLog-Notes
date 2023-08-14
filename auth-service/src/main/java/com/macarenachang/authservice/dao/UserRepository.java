package com.macarenachang.authservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.macarenachang.authservice.model.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
}
