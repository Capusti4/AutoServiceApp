package com.example.AutoServiceApp.DTO;

import java.util.List;

public record FeedbackResponse(
        List<FeedbackDTO> feedbacks
) {
}
