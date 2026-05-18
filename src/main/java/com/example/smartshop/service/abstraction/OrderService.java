package com.example.smartshop.service.abstraction;

import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.request.OrderRequestDTO;
import com.example.smartshop.dto.response.OrderResponseDTO;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(List<OrderItemRequestDTO> items, User user);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getOrders();

    OrderResponseDTO cancelOrder(Long id);

    OrderResponseDTO changeStatus(Long id, OrderStatus newStatus);

    void deleteOrder(Long id);
}
