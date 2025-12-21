package com.example.AutoServiceApp.DTO;

public record NotificationDTO(
        String _id,
        String userId,
        int typeId,
        String text,
        boolean isRead
) {
}
