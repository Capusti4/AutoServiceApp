package com.example.AutoServiceApp.DTO;

import java.util.List;

public record GetNotificationsResponse(
        List<NotificationDTO> notifications
) {
}
