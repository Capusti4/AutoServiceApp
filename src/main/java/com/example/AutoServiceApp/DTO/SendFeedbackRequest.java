package com.example.AutoServiceApp.DTO;


public record SendFeedbackRequest(
        long targetId,
        long orderId,
        int rating,
        String feedback
) {
}
