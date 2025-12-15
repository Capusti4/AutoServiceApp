package com.example.AutoServiceApp;

import com.example.AutoServiceApp.Exceptions.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleAppException(AppException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({Exception.class, JsonProcessingException.class})
    public ResponseEntity<Map<String, String>> handleUnknownException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутренняя ошибка сервера"));
    }
}
