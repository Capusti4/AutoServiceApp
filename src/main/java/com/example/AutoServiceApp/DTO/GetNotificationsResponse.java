package com.example.AutoServiceApp.DTO;

import com.example.AutoServiceApp.Entity.NotificationEntity;

import java.util.List;

public record GetNotificationsResponse(
        List<NotificationEntity> notifications
) {
}
