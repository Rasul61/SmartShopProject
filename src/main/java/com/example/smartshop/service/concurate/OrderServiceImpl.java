package com.example.smartshop.service.concurate;

import com.example.smartshop.dto.mapper.OrderMapper;
import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.request.OrderRequestDTO;
import com.example.smartshop.dto.response.OrderResponseDTO;
import com.example.smartshop.exception.*;
import com.example.smartshop.model.User;
import com.example.smartshop.model.Order;
import com.example.smartshop.model.OrderItem;
import com.example.smartshop.model.Product;
import com.example.smartshop.model.enums.OrderStatus;
import com.example.smartshop.repository.OrderRepository;
import com.example.smartshop.repository.ProductRepository;
import com.example.smartshop.service.abstraction.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponseDTO createOrder(List<OrderItemRequestDTO> items, User user) {

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        Set<Long> productIds = new HashSet<>();

        for (OrderItemRequestDTO item : items) {

            if (!productIds.add(item.getProductId())) {
                throw new DuplicateProductException(item.getProductId());
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow();

            if (product.getQuantity() < item.getQuantity()) {
                throw new ProductOutOfStockException(product.getQuantity());
            }

            product.setQuantity(product.getQuantity() - item.getQuantity());

            BigDecimal itemPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemPrice);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(itemPrice);

            orderItems.add(orderItem);
        }

        if (user.getBalance().compareTo(total) < 0) {
            throw new NotEnoughBalanceException(ErrorCode.NOT_ENOUGH_BALANCE,Order.class.getSimpleName(),user.getBalance());
        }

        user.setBalance(user.getBalance().subtract(total));

        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(LocalDateTime.now());

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        Order orderCreated = orderRepository.save(order);

        return orderMapper.entityToResponse(orderCreated);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND, Order.class.getSimpleName(), id));
        return orderMapper.entityToResponse(order);
    }

    @Override
    public List<OrderResponseDTO> getOrders() {
        return orderRepository.findAll().stream().
                map(orderMapper::entityToResponse).
                toList();
    }




    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public OrderResponseDTO cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        Order.class.getSimpleName(),
                        id
                ));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        User user = order.getUser();

        user.setBalance(
                user.getBalance().add(order.getTotalPrice())
        );

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();

            product.setQuantity(
                    product.getQuantity() + item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        Order cancelledOrder = orderRepository.save(order);

        return orderMapper.entityToResponse(cancelledOrder);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
