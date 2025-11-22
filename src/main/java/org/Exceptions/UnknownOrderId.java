package org.Exceptions;

public class UnknownOrderId extends Exception {
    public UnknownOrderId() {
        super("Неизвестный заказ");
    }
}
