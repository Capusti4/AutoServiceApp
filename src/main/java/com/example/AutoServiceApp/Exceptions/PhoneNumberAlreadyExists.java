package com.example.AutoServiceApp.Exceptions;

public class PhoneNumberAlreadyExists extends AppException {
    public PhoneNumberAlreadyExists() {
      super("Пользователь с таким номером телефона уже существует", 400);
    }
}
