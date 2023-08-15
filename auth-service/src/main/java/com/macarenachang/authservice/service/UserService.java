package com.macarenachang.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.macarenachang.authservice.dao.UserRepository;
import com.macarenachang.authservice.model.Authority;
import com.macarenachang.authservice.model.User;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User registerUser(String email, String password) {
        User newUser = new User(email, password);
        // Encrypt the user's password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Set default role
        Authority userAuthority = new Authority(newUser, "USER");
        newUser.setAuthorities(Set.of(userAuthority));

        // Save the user to the database using UserRepository
        return userRepository.save(newUser);
    }

}
