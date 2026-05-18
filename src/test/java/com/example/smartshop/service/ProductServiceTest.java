package com.example.smartshop.service;

import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Product;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void updateProduct_success() {
        Product product = Product.builder()
                .id(1L)
                .name("Old phone")
                .price(BigDecimal.valueOf(100))
                .quantity(5)
                .category("Old")
                .description("Old description")
                .build();

        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("iPhone")
                .price(BigDecimal.valueOf(1200))
                .quantity(10)
                .category("Electronics")
                .description("New phone")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProduct(1L, request);

        assertEquals("iPhone", product.getName());
        assertEquals(BigDecimal.valueOf(1200), product.getPrice());
        assertEquals(10, product.getQuantity());
        assertEquals("Electronics", product.getCategory());
        assertEquals("New phone", product.getDescription());

        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_notFound_throwException() {
        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("iPhone")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> productService.updateProduct(1L, request)
        );
    }

    @Test
    void deleteProduct_success_softDelete() {
        Product product = Product.builder()
                .id(1L)
                .deleted(false)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deleteProduct(1L);

        assertTrue(product.isDeleted());
        assertNotNull(product.getUpdatedAt());

        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_notFound_throwException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> productService.deleteProduct(1L)
        );
    }
}