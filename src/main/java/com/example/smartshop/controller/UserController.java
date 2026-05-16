package com.example.smartshop.controller;

import com.example.smartshop.dto.request.UserRequestDTO;
import com.example.smartshop.dto.response.UserResponseDTO;
import com.example.smartshop.model.User;
import com.example.smartshop.repository.UserRepository;
import com.example.smartshop.service.abstraction.UserService;
import com.example.smartshop.service.concurate.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;


    @PostMapping("/admin")
    public ResponseEntity<String> createAdmin(
            @RequestBody User newAdmin,
            Authentication authentication) {

        System.out.println("AUTH NAME = " + authentication.getName());
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        authService.createAdmin(newAdmin, currentUser);

        return ResponseEntity.ok("Admin created!");
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @PageableDefault(
                    size = 10,
                    page = 0,
                    sort = "id"
            )
            Pageable pageable) {

        return userService.getUser(username, email, role, pageable);
    }


    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(id, userRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
