package com.example.smartshop.service.abstraction;

import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderItemResponseDTO;

import java.util.List;

public interface OrderItemService {
    OrderItemResponseDTO createOrderItem(OrderItemRequestDTO orderItemRequestDTO);

    OrderItemResponseDTO getOrderItemById(Long id);

    List<OrderItemResponseDTO> getOrderItems();

    OrderItemResponseDTO updateOrder(Long id, OrderItemRequestDTO orderItemRequestDTO);

    void deleteOrderItem(Long id);
}
