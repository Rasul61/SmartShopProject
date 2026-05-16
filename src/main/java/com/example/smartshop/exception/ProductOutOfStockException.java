package com.example.smartshop.exception;

public class ProductOutOfStockException extends BadRequestException {

    public ProductOutOfStockException(Integer availableQuantity) {
        super(ErrorCode.PRODUCT_OUT_OF_STOCK, availableQuantity);
    }
}