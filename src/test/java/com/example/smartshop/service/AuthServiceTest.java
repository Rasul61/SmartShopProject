package com.example.smartshop.service;

import com.example.smartshop.dto.request.RegisterRequest;
import com.example.smartshop.dto.response.AuthResponse;
import com.example.smartshop.model.User;
import com.example.smartshop.repository.UserRepository;
import com.example.smartshop.service.impl.AuthService;
import com.example.smartshop.service.impl.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_userAlreadyExists_throwException() {

        RegisterRequest request = new RegisterRequest();

        request.setName("rasul");
        request.setEmail("rasul@gmail.com");
        request.setPassword("12345");

        User user = User.builder()
                .username("rasul")
                .email("rasul@gmail.com")
                .build();

        when(userRepository.findByEmail("rasul@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );
    }


    @Test
    void register_success() {

        RegisterRequest request = new RegisterRequest();

        request.setName("rasul");
        request.setEmail("rasul@gmail.com");
        request.setPassword("12345");

        when(userRepository.findByEmail("rasul@gmail.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(passwordEncoder.encode("12345"))
                .thenReturn("encodedPassword");

        when(jwtService.generateToken(any(User.class)))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(any(User.class)))
                .thenReturn("refresh-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);

        verify(userRepository).save(any(User.class));
    }
}