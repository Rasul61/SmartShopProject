package com.example.smartshop.controller;

import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderResponseDTO;
import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.OrderStatus;
import com.example.smartshop.repository.UserRepository;
import com.example.smartshop.service.abstraction.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;



    @PostMapping
    public OrderResponseDTO createOrder(
            @RequestBody List<OrderItemRequestDTO> items,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        return orderService.createOrder(items, user);
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }


    @PatchMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
//        http://localhost:8077/api/v1/orders/13/cancel
    }

    @PatchMapping("/{id}/status")
    public OrderResponseDTO changeStatus(@PathVariable Long id,
                                         @RequestParam OrderStatus status) {
        return orderService.changeStatus(id, status);
        //http://localhost:8077/api/v1/orders/5/status?status=SHIPPED
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
