package com.example.AutoServiceApp.DTO;

import java.util.UUID;

public record SendFeedbackRequest(
        String username,
        String sessionToken,
        UUID authorId,
        UUID targetId,
        UUID orderId,
        int rating,
        String feedback
) implements WithUserDataDTO {
}
