package com.example.smartshop.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {
    protected final int status;
    protected ErrorCode errorCode;
    protected final Object[] arguments;

    public GenericException(int status, ErrorCode errorCode, Object[] arguments) {
        super(errorCode.getCode());
        this.status = status;
        this.errorCode = errorCode;
        this.arguments = arguments;
    }
}

