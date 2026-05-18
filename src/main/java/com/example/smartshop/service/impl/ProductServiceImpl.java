package com.example.smartshop.service.impl;

import com.example.smartshop.dto.mapper.ProductMapper;
import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.dto.response.ProductResponseDTO;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Product;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.service.abstraction.ProductService;
import com.example.smartshop.specification.ProductSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = ProductMapper.requestToProduct(productRequestDTO);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return ProductMapper.entityToProduct(productRepository.save(product));
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, Product.class.getSimpleName(), id));

        return ProductMapper.entityToProduct(product);
    }

    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().
                map(ProductMapper::entityToProduct).
                toList();
    }


    @Override
    public Page<ProductResponseDTO> getProduct(
            String name,
            String description,
            BigDecimal price,
            String category,
            Pageable pageable) {

        Specification<Product> spec = Specification
                .where(ProductSpec.hasName(name))
                .and(ProductSpec.hasDescription(description))
                .and(ProductSpec.hasPrice(price))
                .and(ProductSpec.hasCategory(category));

        return productRepository.findAll(spec, pageable)
                .map(ProductMapper::entityToProduct);
    }


    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, Product.class.getSimpleName(), id)
        );

        if (productRequestDTO.getName() != null && !productRequestDTO.getName().isEmpty()) {
            product.setName(productRequestDTO.getName());
        }

        if (productRequestDTO.getQuantity() != null) {
            product.setQuantity(productRequestDTO.getQuantity());
        }

        if (productRequestDTO.getPrice() != null) {
            product.setPrice(productRequestDTO.getPrice());
        }

        if (productRequestDTO.getCategory() != null && !productRequestDTO.getCategory().isEmpty()) {
            product.setCategory(productRequestDTO.getCategory());
        }

        if (productRequestDTO.getDescription() != null && !productRequestDTO.getDescription().isEmpty()) {
            product.setDescription(productRequestDTO.getDescription());
        }

        product.setUpdatedAt(LocalDateTime.now());
        return ProductMapper.entityToProduct(productRepository.save(product));
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        Product.class.getSimpleName(),
                        id
                ));

        product.setDeleted(true);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }
}
