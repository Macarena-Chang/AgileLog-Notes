package com.macarenachang.authservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.macarenachang.authservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRegistrationIntegrationTest {

    @Autowired
    private UserService userService;
    
    private String testEmail = "integration@example.com";
    private String testPassword = "password";
    
    @Test
    void testRegisterUserIntegration() {
        
        User result = userService.registerUser(testEmail, testPassword);
        
        assertNotNull(result);
        assertEquals(testEmail, result.getEmail());
    }
    
    @AfterEach
    void cleanup() {
        User user = userService.findByEmail(testEmail);
        if (user != null) {
            userService.deleteUser(user);
        }
    }
}