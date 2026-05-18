package com.example.smartshop.mapper;

import com.example.smartshop.dto.mapper.WishlistMapper;
import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.model.Product;
import com.example.smartshop.model.Wishlist;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WishlistMapperTest {

    @Test
    void entityToResponse_success() {

        Product product = Product.builder()
                .id(1L)
                .name("iPhone 15")
                .price(BigDecimal.valueOf(1200))
                .category("Electronics")
                .build();

        LocalDateTime createdAt = LocalDateTime.of(2026, 5, 18, 12, 0);

        Wishlist wishlist = Wishlist.builder()
                .id(10L)
                .product(product)
                .createdAt(createdAt)
                .build();

        WishlistResponseDTO response = WishlistMapper.entityToResponse(wishlist);

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals("iPhone 15", response.getProductName());
        assertEquals(BigDecimal.valueOf(1200), response.getProductPrice());
        assertEquals("Electronics", response.getCategory());
        assertEquals(createdAt, response.getCreatedAt());
    }
}