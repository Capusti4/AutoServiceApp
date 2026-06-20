package com.example.AutoServiceApp.DTO;



public record SendFeedbackRequest(
        String username,
        String sessionToken,
        long authorId,
        long targetId,
        long orderId,
        int rating,
        String feedback
) {
}
