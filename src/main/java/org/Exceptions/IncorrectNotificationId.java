package org.Exceptions;

public class IncorrectNotificationId extends RuntimeException {
    public IncorrectNotificationId() {
        super("Некорректный айди уведомления");
    }
}
