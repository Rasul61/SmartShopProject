package com.example.smartshop.dto.mapper;

import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderItemResponseDTO;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.Order;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.model.Product;
import com.example.smartshop.repository.OrderRepository;
import com.example.smartshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderItem requestToOrderItem(OrderItemRequestDTO dto) {
        Product product = productRepo.findById(dto.getProductId()).
                orElseThrow(() ->
                        new NotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND, OrderItem.class.getSimpleName(), dto.getProductId()));


        return OrderItem.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .price(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())))
                .build();
    }

    public OrderItemResponseDTO entityToResponse(OrderItem entity) {
        return OrderItemResponseDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .productId(entity.getProduct().getId())
                .build();
    }
}
