package com.cupabakery.backend.exception;

import com.cupabakery.backend.model.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
     * Handler error from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Lỗi validation dữ liệu: ");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append("[").append(fieldName).append(": ").append(message).append("] ");
        });

        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                errorMessage.toString(),
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
     * Handler cho DataIntegrityViolationException (lỗi ràng buộc DB)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Lỗi ràng buộc dữ liệu";
        String code = "DATA_INTEGRITY_VIOLATION";

        // Phân tích lỗi từ message gốc để cung cấp thông tin chi tiết hơn
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
     * Handler undefined error
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
     * Error response base on environment
     */
    private ErrorResponse createErrorResponse(int status, String code, String message, Throwable ex) {
        // Visible stacktrace when dev environment detect
        String activeProfile = environment.getActiveProfiles().length > 0 ?
                environment.getActiveProfiles()[0] : "prod";

        if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
            return new ErrorResponse(status, code, message, ex);
        } else {
            return new ErrorResponse(status, code, message);
        }
    }

    /**
     * Handler JWT exception
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
     * Handler Bad Credential cause login info not match
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
}
