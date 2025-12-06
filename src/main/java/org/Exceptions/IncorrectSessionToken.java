package org.Exceptions;

public class IncorrectSessionToken extends Exception {
    public IncorrectSessionToken() {
        super("Токен сессии недействителен");
    }
}
