package com.example.AutoServiceApp.Exception;

public class IncorrectUsername extends AppException {
    public IncorrectUsername() {
        super("Ошибка ввода, юзернейм может содержать только английские буквы или цифры и должен начинаться с буквы", 400);
    }
}
