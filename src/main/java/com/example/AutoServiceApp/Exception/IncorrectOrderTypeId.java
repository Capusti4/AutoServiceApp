package com.example.AutoServiceApp.Exception;

public class IncorrectOrderTypeId extends AppException {
    public IncorrectOrderTypeId() {
        super("Неккоректный тип заказа", 400);
    }
}
