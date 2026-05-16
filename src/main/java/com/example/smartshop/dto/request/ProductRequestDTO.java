package com.example.smartshop.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String name;
    @Positive
    private BigDecimal price;
    private String category;
    @Positive
    private Integer quantity;
    private String description;
}