package org.Exceptions;

public class IncorrectPhoneNumber extends RuntimeException {
    public IncorrectPhoneNumber() {
        super("Ошибка ввода, номер телефона должен иметь формат 89201234567");
    }
}
