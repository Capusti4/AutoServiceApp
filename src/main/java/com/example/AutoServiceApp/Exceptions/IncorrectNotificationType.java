package com.example.AutoServiceApp.Exceptions;

public class IncorrectNotificationType extends AppException {
    public IncorrectNotificationType() {
        super("Некорректный тип уведомления", 400);
    }
}
