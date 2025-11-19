package org.Exceptions;

public class IncorrectNotificationType extends RuntimeException {
    public IncorrectNotificationType() {
        super("Некорректный тип уведомления");
    }
}
