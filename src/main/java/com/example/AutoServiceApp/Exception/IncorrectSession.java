package com.example.AutoServiceApp.Exception;

public class IncorrectSession extends AppException {
    public IncorrectSession() {
        super("Сессия была завершена", 401);
    }
}
