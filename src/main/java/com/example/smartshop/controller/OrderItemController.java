package com.example.smartshop.controller;

import com.example.smartshop.dto.request.OrderItemRequestDTO;
import com.example.smartshop.dto.response.OrderItemResponseDTO;
import com.example.smartshop.service.abstraction.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order_items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemResponseDTO saveOrderItem(@RequestBody OrderItemRequestDTO orderItem) {
        return orderItemService.createOrderItem(orderItem);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderItemResponseDTO getOrderItem(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }


    @GetMapping
    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItemService.getOrderItems();
    }

    @PutMapping("/{id}")
    public OrderItemResponseDTO updateOrderItem(@PathVariable Long id, @RequestBody OrderItemRequestDTO orderItem) {
        return orderItemService.updateOrder(id, orderItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
    }
}

