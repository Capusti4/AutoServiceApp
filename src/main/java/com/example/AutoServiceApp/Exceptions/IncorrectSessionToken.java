package com.example.AutoServiceApp.Exceptions;

public class IncorrectSessionToken extends AppException {
    public IncorrectSessionToken() {
        super("Токен сессии недействителен", 401);
    }
}
