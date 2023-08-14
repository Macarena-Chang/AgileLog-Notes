package com.macarenachang.authservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.macarenachang.authservice.dao.UserRepository;
import com.macarenachang.authservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        // Check if email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Hash the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    } 


    
}

