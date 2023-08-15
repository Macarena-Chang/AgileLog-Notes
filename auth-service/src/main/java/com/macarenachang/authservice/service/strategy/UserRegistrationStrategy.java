package com.macarenachang.authservice.service.strategy;

import com.macarenachang.authservice.model.User;

public interface UserRegistrationStrategy {
    User register(String email, String password);
}