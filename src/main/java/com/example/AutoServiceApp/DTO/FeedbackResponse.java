package com.example.AutoServiceApp.DTO;

import com.example.AutoServiceApp.Entity.FeedbackEntity;

import java.util.List;

public record FeedbackResponse(
        List<FeedbackEntity> feedbacks
) {
}
