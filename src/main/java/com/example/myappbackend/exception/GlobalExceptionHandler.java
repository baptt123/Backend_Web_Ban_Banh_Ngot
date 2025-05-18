package com.example.myappbackend.exception;

import com.example.myappbackend.dto.response.ErrorResponse;
import io.jsonwebtoken.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    @Autowired
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Handler cho BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handler @Valid validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Lỗi validation dữ liệu: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append("[").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("] ");
        }

        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                errorMessage.toString().trim(),
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler cho ResponseStatusException
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getStatusCode().value(),
                "API_ERROR",
                ex.getReason(),
                ex
        );
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    /**
     * Handler lỗi DB ràng buộc dữ liệu
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Lỗi ràng buộc dữ liệu";
        String code = "DATA_INTEGRITY_VIOLATION";

        String rootCauseMessage = ex.getMostSpecificCause().getMessage();
        if (rootCauseMessage != null) {
            if (rootCauseMessage.contains("Duplicate entry") && rootCauseMessage.contains("username")) {
                message = "Username đã được sử dụng";
                code = "USERNAME_ALREADY_EXISTS";
            } else if (rootCauseMessage.contains("Duplicate entry") && rootCauseMessage.contains("email")) {
                message = "Email đã được sử dụng";
                code = "EMAIL_ALREADY_EXISTS";
            } else if (rootCauseMessage.contains("Duplicate entry") && rootCauseMessage.contains("phone")) {
                message = "Số điện thoại đã được sử dụng";
                code = "PHONE_ALREADY_EXISTS";
            } else if (rootCauseMessage.contains("foreign key constraint")) {
                message = "Lỗi khóa ngoại: Dữ liệu đang được tham chiếu bởi bảng khác";
                code = "FOREIGN_KEY_VIOLATION";
            } else if (rootCauseMessage.contains("check constraint")) {
                message = "Lỗi ràng buộc kiểm tra: Dữ liệu không thỏa mãn điều kiện";
                code = "CHECK_CONSTRAINT_VIOLATION";
            } else if (rootCauseMessage.contains("not-null constraint")) {
                message = "Lỗi ràng buộc không được null";
                code = "NOT_NULL_VIOLATION";
            } else if (rootCauseMessage.contains("unique constraint")) {
                message = "Lỗi ràng buộc duy nhất: Dữ liệu đã tồn tại";
                code = "UNIQUE_CONSTRAINT_VIOLATION";
            }
        }

        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                code,
                message,
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler cho EntityExistsException
     */
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.CONFLICT.value(),
                "ENTITY_EXISTS",
                ex.getMessage(),
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handler cho EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ENTITY_NOT_FOUND",
                ex.getMessage(),
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler cho ResourceNotFoundException (tùy chỉnh)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler cho lỗi tạo đơn hàng thất bại
     */
    @ExceptionHandler(OrderNotCreateException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotCreate(OrderNotCreateException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "ORDER_CREATE_FAILED",
                ex.getMessage(),
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_CREDENTIALS",
                "Username hoặc mật khẩu không đúng.",
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler cho JWT các loại
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        String errorCode = "JWT_ERROR";
        String message = "Lỗi xác thực token";

        if (ex instanceof ExpiredJwtException) {
            errorCode = "JWT_EXPIRED";
            message = "Token JWT đã hết hạn";
        } else if (ex instanceof MalformedJwtException) {
            errorCode = "JWT_MALFORMED";
            message = "Token JWT không hợp lệ";
        } else if (ex instanceof UnsupportedJwtException) {
            errorCode = "JWT_UNSUPPORTED";
            message = "Token JWT không được hỗ trợ";
        } else if (ex instanceof SignatureException) {
            errorCode = "JWT_SIGNATURE_INVALID";
            message = "Chữ ký JWT không hợp lệ";
        }

        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                errorCode,
                message,
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler fallback cho các lỗi chưa xác định
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.",
                ex
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Tạo ErrorResponse tùy theo môi trường
     */
    private ErrorResponse createErrorResponse(int status, String code, String message, Throwable ex) {
        String activeProfile = environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0] : "prod";

        if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
            return new ErrorResponse(status, code, message, ex);
        } else {
            return new ErrorResponse(status, code, message);
        }
    }
}
