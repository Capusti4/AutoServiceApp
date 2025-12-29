package com.example.AutoServiceApp.Exception;

public class AppException extends RuntimeException {
    private final int httpStatus;

    public AppException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
