package com.example.smartshop.service.impl;

import com.example.smartshop.dto.mapper.WishlistMapper;
import com.example.smartshop.dto.response.WishlistResponseDTO;
import com.example.smartshop.exception.BadRequestException;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Product;
import com.example.smartshop.model.User;
import com.example.smartshop.model.Wishlist;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.repository.WishlistRepository;
import com.example.smartshop.service.abstraction.WishlistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public WishlistResponseDTO addToWishlist(Long productId, User user) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        Product.class.getSimpleName(),
                        productId
                ));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        return WishlistMapper.entityToResponse(wishlistRepository.save(wishlist));
    }

    @Override
    public List<WishlistResponseDTO> getMyWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .stream()
                .map(WishlistMapper::entityToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long productId, User user) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        Product.class.getSimpleName(),
                        productId
                ));

        wishlistRepository.deleteByUserAndProduct(user, product);
    }
}