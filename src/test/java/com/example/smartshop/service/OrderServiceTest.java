package com.example.smartshop.service;

import com.example.smartshop.dto.mapper.OrderMapper;
import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderResponseDTO;
import com.example.smartshop.exception.DuplicateProductException;
import com.example.smartshop.exception.NotEnoughBalanceException;
import com.example.smartshop.exception.ProductOutOfStockException;
import com.example.smartshop.model.Order;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.model.Product;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.OrderStatus;
import com.example.smartshop.repository.OrderRepository;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_success() {

        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .quantity(10)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setProductId(1L);
        item.setQuantity(2);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(new Order());

//        when(orderMapper.entityToResponse(any(Order.class)))
//                .thenReturn(new OrderResponseDTO());

        OrderResponseDTO response = orderService.createOrder(
                List.of(item),
                user
        );

//        assertNotNull(response);

        assertEquals(
                BigDecimal.valueOf(800),
                user.getBalance()
        );

        assertEquals(
                8,
                product.getQuantity()
        );
    }

    @Test
    void createOrder_notEnoughBalance_throwException() {

        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(1000))
                .quantity(10)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setProductId(1L);
        item.setQuantity(2);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                NotEnoughBalanceException.class,
                () -> orderService.createOrder(
                        List.of(item),
                        user
                )
        );
    }

    @Test
    void createOrder_duplicateProduct_throwException() {

        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .quantity(10)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        OrderItemRequestDTO item1 = new OrderItemRequestDTO();
        item1.setProductId(1L);
        item1.setQuantity(1);

        OrderItemRequestDTO item2 = new OrderItemRequestDTO();
        item2.setProductId(1L);
        item2.setQuantity(2);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product))
                .thenReturn(Optional.of(product));

        assertThrows(
                DuplicateProductException.class,
                () -> orderService.createOrder(List.of(item1, item2), user)
        );
    }

    @Test
    void createOrder_outOfStock_throwException() {

        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .quantity(1)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setProductId(1L);
        item.setQuantity(5);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                ProductOutOfStockException.class,
                () -> orderService.createOrder(
                        List.of(item),
                        user
                )
        );
    }

    @Test
    void cancelOrder_success() {

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        Product product = Product.builder()
                .quantity(5)
                .build();

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .items(List.of(item))
                .totalPrice(BigDecimal.valueOf(200))
                .status(OrderStatus.PAID)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

//        when(orderMapper.entityToResponse(any(Order.class)))
//                .thenReturn(new OrderResponseDTO());

        OrderResponseDTO response = orderService.cancelOrder(1L);

//        assertNotNull(response);

        assertEquals(
                BigDecimal.valueOf(300),
                user.getBalance()
        );

        assertEquals(
                8,
                product.getQuantity()
        );

        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus()
        );
    }
}