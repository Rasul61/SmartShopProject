package com.example.smartshop.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class OrderItemResponseDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
   // private Long orderId;
    private BigDecimal price;
}