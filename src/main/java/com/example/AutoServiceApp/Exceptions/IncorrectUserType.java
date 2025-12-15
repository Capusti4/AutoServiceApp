package com.example.AutoServiceApp.Exceptions;

public class IncorrectUserType extends AppException {
    public IncorrectUserType() {
        super("Некорректный тип пользователя", 400);
    }
}
