package com.example.smartshop.service;

import com.example.smartshop.dto.mapper.OrderItemMapper;
import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.exception.NotFoundException;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.model.Product;
import com.example.smartshop.repository.OrderItemRepository;
import com.example.smartshop.service.impl.OrderItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void getOrderItemById_success() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        orderItemService.getOrderItemById(1L);

        verify(orderItemMapper).entityToResponse(orderItem);
    }

    @Test
    void getOrderItemById_notFound_throwException() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> orderItemService.getOrderItemById(1L)
        );
    }

    @Test
    void updateOrderItem_success() {
        Product product = Product.builder()
                .price(BigDecimal.valueOf(100))
                .build();

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        OrderItemRequestDTO request = new OrderItemRequestDTO();
        request.setQuantity(3);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        orderItemService.updateOrder(1L, request);

        assertEquals(BigDecimal.valueOf(300), orderItem.getPrice());

        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void deleteOrderItem_success() {
        orderItemService.deleteOrderItem(1L);

        verify(orderItemRepository).deleteById(1L);
    }
}