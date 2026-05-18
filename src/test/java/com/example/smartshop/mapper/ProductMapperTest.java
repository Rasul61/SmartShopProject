package com.example.smartshop.mapper;

import com.example.smartshop.dto.mapper.ProductMapper;
import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.dto.response.ProductResponseDTO;
import com.example.smartshop.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void requestToProduct_success() {

        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("iPhone")
                .description("Phone")
                .price(BigDecimal.valueOf(1200))
                .quantity(5)
                .category("Electronics")
                .build();

        Product product = ProductMapper.requestToProduct(request);

        assertEquals("iPhone", product.getName());
        assertEquals("Phone", product.getDescription());
        assertEquals(BigDecimal.valueOf(1200), product.getPrice());
        assertEquals(5, product.getQuantity());
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    void entityToProduct_success() {

        Product product = Product.builder()
                .id(1L)
                .name("iPhone")
                .description("Phone")
                .price(BigDecimal.valueOf(1200))
                .quantity(5)
                .category("Electronics")
                .build();

        ProductResponseDTO response =
                ProductMapper.entityToProduct(product);

        assertEquals(1L, response.getId());
        assertEquals("iPhone", response.getName());
        assertEquals(BigDecimal.valueOf(1200), response.getPrice());
    }
}