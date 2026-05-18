package com.example.smartshop.service.abstraction;

import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.model.User;

import java.util.List;

public interface WishlistService {

    WishlistResponseDTO addToWishlist(Long productId, User user);

    List<WishlistResponseDTO> getMyWishlist(User user);

    void removeFromWishlist(Long productId, User user);
}