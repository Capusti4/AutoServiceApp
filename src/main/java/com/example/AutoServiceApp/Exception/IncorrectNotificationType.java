package com.example.AutoServiceApp.Exception;

public class IncorrectNotificationType extends AppException {
    public IncorrectNotificationType() {
        super("Некорректный тип уведомления", 400);
    }
}
