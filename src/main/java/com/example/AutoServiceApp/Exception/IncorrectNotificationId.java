package com.example.AutoServiceApp.Exception;

public class IncorrectNotificationId extends AppException {
    public IncorrectNotificationId() {
        super("Некорректный айди уведомления", 400);
    }
}
