package com.example.AutoServiceApp.Exception;

public class IncorrectSessionToken extends AppException {
    public IncorrectSessionToken() {
        super("Токен сессии недействителен", 401);
    }
}
