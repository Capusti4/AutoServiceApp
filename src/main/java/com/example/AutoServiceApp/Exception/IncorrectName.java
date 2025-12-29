package com.example.AutoServiceApp.Exception;

public class IncorrectName extends AppException {
    public IncorrectName() {
        super("Ошибка ввода, имя или фамилия содержит недопустимые символы", 400);
    }
}
