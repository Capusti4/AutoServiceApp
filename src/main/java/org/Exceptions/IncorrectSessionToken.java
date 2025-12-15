package org.Exceptions;

public class IncorrectSessionToken extends AppException {
    public IncorrectSessionToken() {
        super("Токен сессии недействителен", 401);
    }
}
