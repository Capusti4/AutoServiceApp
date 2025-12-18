package com.example.AutoServiceApp.DTO;

public record SendFeedbackRequest(
        String username,
        String sessionToken,
        String authorId,
        String targetId,
        String orderId,
        int rating,
        String comment
) implements WithUserDataDTO {
}
