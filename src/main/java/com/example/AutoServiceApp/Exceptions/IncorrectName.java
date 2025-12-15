package com.example.AutoServiceApp.Exceptions;

public class IncorrectName extends AppException {
    public IncorrectName() {
        super("Ошибка ввода, имя или фамилия содержит недопустимые символы", 400);
    }
}
