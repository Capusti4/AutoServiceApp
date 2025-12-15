package com.example.AutoServiceApp.Exceptions;

public class IncorrectOrderTypeId extends AppException {
    public IncorrectOrderTypeId() {
        super("Неккоректный тип заказа", 400);
    }
}
