package org.Exceptions;

public class IncorrectUsernameOrPassword extends AppException {
    public IncorrectUsernameOrPassword() {
        super("Логин и/или пароль не верный", 400);
    }
}
