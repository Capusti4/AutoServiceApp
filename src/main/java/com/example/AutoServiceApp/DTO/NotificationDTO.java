package com.example.AutoServiceApp.DTO;

import org.bson.types.ObjectId;

public record NotificationDTO(
        ObjectId id,
        ObjectId userId,
        int typeId,
        String text,
        boolean isRead
) {
}
