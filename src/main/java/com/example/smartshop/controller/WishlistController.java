package com.example.smartshop.controller;

import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.model.User;
import com.example.smartshop.repository.UserRepository;
import com.example.smartshop.service.abstraction.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponseDTO addToWishlist(@PathVariable Long productId,
                                             Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        return wishlistService.addToWishlist(productId, user);
    }

    @GetMapping
    public List<WishlistResponseDTO> getMyWishlist(Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        return wishlistService.getMyWishlist(user);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWishlist(@PathVariable Long productId,
                                   Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        wishlistService.removeFromWishlist(productId, user);
    }
}