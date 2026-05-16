package com.example.smartshop.exception;

public class NotEnoughBalanceException extends BadRequestException {

    public NotEnoughBalanceException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
        this.errorCode = errorCode;
    }
}