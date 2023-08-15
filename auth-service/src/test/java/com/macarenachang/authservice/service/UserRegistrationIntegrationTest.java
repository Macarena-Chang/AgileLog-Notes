package com.macarenachang.authservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.macarenachang.authservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRegistrationIntegrationTest {

    @Autowired
    private UserService userService;
    
    @Test
    void testRegisterUserIntegration() {
        String email = "integration@example.com";
        String password = "password";
        
        User result = userService.registerUser(email, password);
        
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }
}
