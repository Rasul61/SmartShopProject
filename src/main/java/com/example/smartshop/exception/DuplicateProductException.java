package com.example.smartshop.exception;

public class DuplicateProductException extends BadRequestException {

    public DuplicateProductException(Long productId) {
        super(ErrorCode.DUPLICATE_PRODUCT, productId);
    }
}