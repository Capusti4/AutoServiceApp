package org.Exceptions;

public class IncorrectName extends RuntimeException {
    public IncorrectName() {
        super("Ошибка ввода, имя или фамилия содержит недопустимые символы");
    }
}
