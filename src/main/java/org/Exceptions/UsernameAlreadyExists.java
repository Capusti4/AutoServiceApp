package org.Exceptions;

public class UsernameAlreadyExists extends AppException {
    public UsernameAlreadyExists() {
        super("Пользователь с таким юзернеймом уже существует", 400);
    }
}
