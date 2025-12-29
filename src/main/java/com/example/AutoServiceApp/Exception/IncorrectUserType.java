package com.example.AutoServiceApp.Exception;

public class IncorrectUserType extends AppException {
    public IncorrectUserType() {
        super("Некорректный тип пользователя", 400);
    }
}
