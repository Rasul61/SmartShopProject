package com.example.smartshop.service.impl;

import com.example.smartshop.dto.mapper.OrderItemMapper;
import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderItemResponseDTO;
import com.example.smartshop.exception.ErrorCode;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.repository.OrderItemRepository;
import com.example.smartshop.service.abstraction.OrderItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemResponseDTO createOrderItem(OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemMapper.requestToOrderItem(orderItemRequestDTO);
        orderItem.setOrder(orderItem.getOrder());
        return orderItemMapper.entityToResponse(orderItemRepository.save(orderItem));
    }

    @Override
    public OrderItemResponseDTO getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND, OrderItem.class.getSimpleName(), id))
                ;
        return orderItemMapper.entityToResponse(orderItem);
    }

    @Override
    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItemRepository.findAll().stream().
                map(orderItemMapper::entityToResponse).
                toList();
    }

    @Override
    @Transactional
    public OrderItemResponseDTO updateOrder(Long id, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND, OrderItem.class.getSimpleName(), id));

        if (orderItemRequestDTO.getQuantity() != null) {
            orderItemRequestDTO.setQuantity(orderItem.getQuantity());
        }

        BigDecimal newPrice = orderItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(orderItemRequestDTO.getQuantity()));
        orderItem.setPrice(newPrice);
        return orderItemMapper.entityToResponse(orderItemRepository.save(orderItem));
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);

    }
}
