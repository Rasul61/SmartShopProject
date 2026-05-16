package com.example.smartshop.dto.mapper;

import com.example.smartshop.dto.request.UserRequestDTO;
import com.example.smartshop.dto.response.UserResponseDTO;
import com.example.smartshop.model.User;

import java.time.LocalDateTime;

public class UserMapper {

    public static User requestToEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .build();               //rol poka netu
    }

    public static UserResponseDTO entityToResponse(User user) {
        return UserResponseDTO.builder().id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
