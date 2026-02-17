package com.example.AutoServiceApp.Exception;

public class IncorrectFeedbackType extends AppException {
    public IncorrectFeedbackType() {
        super("Некорректный тип отзыва", 400);
    }
}
