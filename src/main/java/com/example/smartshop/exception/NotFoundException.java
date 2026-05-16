package com.example.smartshop.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends GenericException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(404, errorCode, args);
        this.errorCode = errorCode;
    }


}
