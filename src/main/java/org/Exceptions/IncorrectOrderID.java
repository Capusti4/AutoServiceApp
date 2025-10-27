package org.Exceptions;

public class IncorrectOrderID extends RuntimeException {
    public IncorrectOrderID() {
        super("Неккоректный тип заказа");
    }
}
