package com.example.smartshop.dto.mapper;

import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.model.Wishlist;

public class WishlistMapper {

    public static WishlistResponseDTO entityToResponse(Wishlist wishlist) {
        return WishlistResponseDTO.builder()
                .id(wishlist.getId())
                .productId(wishlist.getProduct().getId())
                .productName(wishlist.getProduct().getName())
                .productPrice(wishlist.getProduct().getPrice())
                .category(wishlist.getProduct().getCategory())
                .createdAt(wishlist.getCreatedAt())
                .build();
    }
}