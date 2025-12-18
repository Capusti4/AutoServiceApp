package com.example.AutoServiceApp.DTO;

import org.bson.types.ObjectId;

public record FeedbackDTO(
        ObjectId id,
        ObjectId authorId,
        ObjectId targetId,
        int rating,
        String comment
) {
}
