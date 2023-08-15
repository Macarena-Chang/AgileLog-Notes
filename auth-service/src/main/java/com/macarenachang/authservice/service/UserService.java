package com.macarenachang.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macarenachang.authservice.dao.UserRepository;
import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.service.strategy.UserRegistrationStrategy;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationStrategy registrationStrategy;

    @Autowired
    public UserService(UserRepository userRepository, UserRegistrationStrategy registrationStrategy) {
        this.userRepository = userRepository;
        this.registrationStrategy = registrationStrategy;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(String email, String password) {
        return registrationStrategy.register(email, password);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
