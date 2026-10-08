package com.wc2026.common.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {
    private final int status;
    private final String errorType;

    public BusinessRuleException(int status, String errorType, String message) {
        super(message);
        this.status = status;
        this.errorType = errorType;
    }
}
