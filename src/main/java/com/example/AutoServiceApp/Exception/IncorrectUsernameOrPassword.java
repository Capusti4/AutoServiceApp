package com.example.AutoServiceApp.Exception;

public class IncorrectUsernameOrPassword extends AppException {
    public IncorrectUsernameOrPassword() {
        super("Логин и/или пароль не верный", 400);
    }
}
