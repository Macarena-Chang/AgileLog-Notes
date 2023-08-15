package com.macarenachang.authservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.service.strategy.UserRegistrationStrategy;
class UserServiceTest {

    @Mock
    private UserRegistrationStrategy userRegistrationStrategy;

    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User(email, password);
        
        when(userRegistrationStrategy.register(email, password)).thenReturn(user);
        
        // Act
        User result = userService.registerUser(email, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRegistrationStrategy, times(1)).register(email, password);
    }
}
