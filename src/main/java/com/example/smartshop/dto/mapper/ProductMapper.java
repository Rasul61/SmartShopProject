package com.example.smartshop.dto.mapper;

import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.dto.response.ProductResponseDTO;
import com.example.smartshop.model.Product;

public class ProductMapper {

    public static Product requestToProduct(ProductRequestDTO dto) {
        return Product.builder().name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(dto.getCategory())
                .build();

    }

    public static ProductResponseDTO entityToProduct(Product entity) {
        return ProductResponseDTO.builder().id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .category(entity.getCategory())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
