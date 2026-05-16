package com.example.smartshop.exception;

public class BadRequestException extends GenericException {
    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(400, errorCode, args);
    }
}