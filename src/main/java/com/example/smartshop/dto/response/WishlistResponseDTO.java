package com.example.smartshop.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WishlistResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String category;
    private LocalDateTime createdAt;
}