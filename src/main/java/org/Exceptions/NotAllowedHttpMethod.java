package org.Exceptions;

public class NotAllowedHttpMethod extends Exception {
    public NotAllowedHttpMethod() {
        super("Метод не разрешен");
    }
}
