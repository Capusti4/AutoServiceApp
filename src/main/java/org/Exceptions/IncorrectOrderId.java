package org.Exceptions;

public class IncorrectOrderId extends AppException {
    public IncorrectOrderId() {
        super("Некорректный айди заказа", 400);
    }
}
