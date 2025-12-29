package com.example.AutoServiceApp.Exception;

public class IncorrectPhoneNumber extends AppException {
    public IncorrectPhoneNumber() {
        super("Ошибка ввода, номер телефона должен иметь формат 89201234567", 400);
    }
}
