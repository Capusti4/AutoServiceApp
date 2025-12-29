package com.example.AutoServiceApp;

import com.example.AutoServiceApp.Exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger =
            Logger.getLogger(GlobalExceptionHandler.class.getName());
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleAppException(AppException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({Exception.class, JsonProcessingException.class})
    public ResponseEntity<Map<String, String>> handleUnknownException(Exception e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутренняя ошибка сервера"));
    }
}
