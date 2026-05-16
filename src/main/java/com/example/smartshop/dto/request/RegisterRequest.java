package com.example.smartshop.dto.request;

import com.example.smartshop.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    @Email
    private String email;
    private String password;
    private Role role;
}