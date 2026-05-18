package com.example.smartshop.service.impl;

import com.example.smartshop.dto.request.AuthRequest;
import com.example.smartshop.dto.request.RegisterRequest;
import com.example.smartshop.dto.response.AuthResponse;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.Role;
import com.example.smartshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        repository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("User with this email already exists!");
        });

        var user = User.builder()
                .username(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .balance(BigDecimal.valueOf(0))
                .build();

        repository.save(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void createAdmin(User newAdminData, User currentUser) {
        if (currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Access Denied: Only SUPER_ADMIN can create admins");
        }

        if (repository.existsByEmail(newAdminData.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        newAdminData.setRole(Role.ADMIN);
        newAdminData.setPassword(passwordEncoder.encode(newAdminData.getPassword()));
        newAdminData.setCreatedAt(currentUser.getCreatedAt());
        newAdminData.setBalance(currentUser.getBalance());

        repository.save(newAdminData);
    }
}