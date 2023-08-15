package com.macarenachang.authservice.service.strategy;

import com.macarenachang.authservice.model.Authority;
import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.dao.UserRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultUserRegistration implements UserRegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserRegistration(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

     @Override
    @Transactional
    public User register(String email, String password) {
        User newUser = new User(email, password);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Authority userAuthority = new Authority(newUser, "USER");
        newUser.setAuthorities(Set.of(userAuthority));
        return userRepository.save(newUser);
    }
}