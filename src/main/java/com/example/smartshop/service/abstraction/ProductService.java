package com.example.smartshop.service.abstraction;

import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getProducts();

    Page<ProductResponseDTO> getProduct(
            String name,
            String description,
            BigDecimal price,
            String category,
            Pageable pageable);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);
}
