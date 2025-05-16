package com.cupabakery.backend.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BusinessException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public static BusinessException badRequest(String message, String errorCode) {
        return new BusinessException(message, errorCode, HttpStatus.BAD_REQUEST);
    }

    public static BusinessException conflict(String message, String errorCode) {
        return new BusinessException(message, errorCode, HttpStatus.CONFLICT);
    }

    public static BusinessException notFound(String message, String errorCode) {
        return new BusinessException(message, errorCode, HttpStatus.NOT_FOUND);
    }

    public static BusinessException unauthorized(String message, String errorCode) {
        return new BusinessException(message, errorCode, HttpStatus.UNAUTHORIZED);
    }

    public static BusinessException forbidden(String message, String errorCode) {
        return new BusinessException(message, errorCode, HttpStatus.FORBIDDEN);
    }
}
