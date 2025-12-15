package com.example.AutoServiceApp.Exceptions;

public class NotAllowedHttpMethod extends AppException {
    public NotAllowedHttpMethod() {
        super("Метод не разрешен", 405);
    }
}
