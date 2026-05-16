package com.example.smartshop.dto.mapper;

import com.example.smartshop.dto.request.OrderRequestDTO;
import com.example.smartshop.dto.response.OrderItemResponseDTO;
import com.example.smartshop.dto.response.OrderResponseDTO;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Order;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.OrderStatus;
import com.example.smartshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper; // чтобы маппить items


    public OrderResponseDTO entityToResponse(Order entity) {
        List<OrderItemResponseDTO> items = entity.getItems().stream()
                .map(orderItemMapper::entityToResponse)  // маппим каждый OrderItem
                .toList();

        return OrderResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .items(items)
                .totalPrice(entity.getTotalPrice())
                .status(String.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
