package org.Exceptions;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists() {
        super("Пользователь с таким юзернеймом уже существует");
    }
}
