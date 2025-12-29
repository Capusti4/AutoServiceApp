package com.example.AutoServiceApp.Exception;

import org.springframework.http.HttpStatus;

public class IncorrectFeedbackRating extends AppException {
    public IncorrectFeedbackRating() {
        super("Неккоректный рейтинг отзыва", HttpStatus.BAD_REQUEST.value());
    }
}
