package org.Exceptions;

public class IncorrectNotificationType extends AppException {
    public IncorrectNotificationType() {
        super("Некорректный тип уведомления", 400);
    }
}
