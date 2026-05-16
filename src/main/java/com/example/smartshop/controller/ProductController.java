package com.example.smartshop.controller;

import com.example.smartshop.dto.request.ProductRequestDTO;
import com.example.smartshop.dto.response.ProductResponseDTO;
import com.example.smartshop.service.abstraction.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO product) {
        return productService.createProduct(product);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping()
    public Page<ProductResponseDTO> getProductsByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal price,
            @PageableDefault(
                    size = 10,
                    page = 0,
                    sort = "id"
            )
            Pageable pageable) {
        return productService.getProduct(category,name,price,description,pageable);

    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id,@RequestBody ProductRequestDTO product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
