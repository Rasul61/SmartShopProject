package com.example.smartshop.service.concurate;

import com.example.smartshop.dto.mapper.UserMapper;
import com.example.smartshop.dto.request.UserRequestDTO;
import com.example.smartshop.dto.response.UserResponseDTO;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.Role;
import com.example.smartshop.repository.UserRepository;
import com.example.smartshop.service.abstraction.UserService;
import com.example.smartshop.specification.UserSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = UserMapper.requestToEntity(userRequestDTO);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        return UserMapper.entityToResponse(userRepository.save(user));
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, User.class.getSimpleName(), id));

        return UserMapper.entityToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepository.findAll().stream().
                map(UserMapper::entityToResponse).
                toList();
    }

    @Override
    public Page<UserResponseDTO> getUser(
            String username,
            String email,
            String role,
            Pageable pageable) {

        Specification<User> spec = Specification
                .where(UserSpec.hasUsername(username))
                .and(UserSpec.hasEmail(email))
                .and(UserSpec.hasRole(role));

        return userRepository.findAll(spec, pageable)
                .map(UserMapper::entityToResponse);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, User.class.getSimpleName(), id)
        );
        if (userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().isEmpty()) {
            user.setUsername(user.getUsername());
        }

        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().isEmpty()) {
            user.setEmail(user.getEmail());
        }

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(user.getPassword());
        }

        return UserMapper.entityToResponse(userRepository.save(user));
    }



    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
