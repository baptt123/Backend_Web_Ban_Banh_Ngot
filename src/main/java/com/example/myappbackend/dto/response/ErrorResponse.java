package com.example.myappbackend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
// Custom error response for global error handler
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<String> stackTrace;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(int status, String code, String message, Throwable ex) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.stackTrace = extractStackTrace(ex);
    }

    private List<String> extractStackTrace(Throwable ex) {
        if (ex == null) {
            return Collections.emptyList();
        }

        StackTraceElement[] stackElements = ex.getStackTrace();
        List<String> stackTraceList = new ArrayList<>();

        for (StackTraceElement element : stackElements) {
            stackTraceList.add(element.toString());
        }

        return stackTraceList;
    }
}
