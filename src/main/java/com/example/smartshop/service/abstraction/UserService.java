package com.example.smartshop.service.abstraction;

import com.example.smartshop.dto.request.UserRequestDTO;
import com.example.smartshop.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    Page<UserResponseDTO> getUser(String username, String email, String role,Pageable pageable);
}
