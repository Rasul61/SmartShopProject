package com.example.smartshop.service;

import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.exception.BadRequestException;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Product;
import com.example.smartshop.model.User;
import com.example.smartshop.model.Wishlist;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.repository.WishlistRepository;
import com.example.smartshop.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void addToWishlist_success() {

        User user = User.builder()
                .id(1L)
                .username("rasul")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("iPhone")
                .price(BigDecimal.valueOf(1200))
                .category("Electronics")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id(1L)
                .user(user)
                .product(product)
                .build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(wishlistRepository.existsByUserAndProduct(user, product))
                .thenReturn(false);

        when(wishlistRepository.save(any(Wishlist.class)))
                .thenReturn(wishlist);

        WishlistResponseDTO response = wishlistService.addToWishlist(1L, user);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals("iPhone", response.getProductName());

        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void addToWishlist_productNotFound_throwException() {

        User user = User.builder()
                .id(1L)
                .build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> wishlistService.addToWishlist(1L, user)
        );
    }

    @Test
    void addToWishlist_duplicate_throwException() {

        User user = User.builder()
                .id(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(wishlistRepository.existsByUserAndProduct(user, product))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> wishlistService.addToWishlist(1L, user)
        );
    }

    @Test
    void getMyWishlist_success() {

        User user = User.builder()
                .id(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("iPhone")
                .price(BigDecimal.valueOf(1200))
                .category("Electronics")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id(1L)
                .user(user)
                .product(product)
                .build();

        when(wishlistRepository.findByUser(user))
                .thenReturn(List.of(wishlist));

        List<WishlistResponseDTO> result = wishlistService.getMyWishlist(user);

        assertEquals(1, result.size());
        assertEquals("iPhone", result.get(0).getProductName());
    }

    @Test
    void removeFromWishlist_success() {

        User user = User.builder()
                .id(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        wishlistService.removeFromWishlist(1L, user);

        verify(wishlistRepository)
                .deleteByUserAndProduct(user, product);
    }

    @Test
    void removeFromWishlist_productNotFound_throwException() {

        User user = User.builder()
                .id(1L)
                .build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> wishlistService.removeFromWishlist(1L, user)
        );
    }
}