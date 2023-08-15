package com.macarenachang.authservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.macarenachang.authservice.model.Authority;
import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.service.strategy.DefaultUserRegistration;
import com.macarenachang.authservice.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

class DefaultUserRegistrationTest {

    //Tests para DefaultUserRegistration

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private DefaultUserRegistration defaultUserRegistration;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        User user = new User(email, password);
        User savedUser = new User(email, encodedPassword);
        savedUser.setAuthorities(Set.of(new Authority(savedUser, "USER")));
        
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        User result = defaultUserRegistration.register(email, password);
        
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
