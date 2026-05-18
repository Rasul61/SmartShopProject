package com.example.smartshop.model.enums;

public enum OrderStatus {
    PENDING,     // заказ создан, но ещё не оплачен
    PAID,        // оплачен
    SHIPPED,     // отправлен
    DELIVERED,   // доставлен
    CANCELLED    // отменён
}