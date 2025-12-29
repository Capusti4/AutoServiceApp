package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.GetNotificationsResponse;
import com.example.AutoServiceApp.Entity.NotificationEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectNotificationId;
import com.example.AutoServiceApp.Exception.IncorrectNotificationType;
import com.example.AutoServiceApp.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void deleteNotification(UserEntity user, UUID notificationId) {
        List<NotificationEntity> notifications = user.getNotifications();
        for (NotificationEntity notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notificationRepository.delete(notification);
                return;
            }
        }
        throw new IncorrectNotificationId();
    }

    public void readNotification(UserEntity user, UUID notificationId) {
        List<NotificationEntity> notifications = user.getNotifications();
        for (NotificationEntity notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.read();
                return;
            }
        }
        throw new IncorrectNotificationId();
    }

    public void unreadNotification(UserEntity user, UUID notificationId) {
        List<NotificationEntity> notifications = user.getNotifications();
        for (NotificationEntity notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.unread();
                return;
            }
        }
        throw new IncorrectNotificationId();
    }

    public void readAllNotifications(UserEntity user) {
        List<NotificationEntity> notifications = user.getNotifications();
        for (NotificationEntity notification : notifications) {
            notification.read();
        }
    }

    public int getNotificationsAmount(UserEntity user) {
        List<NotificationEntity> notifications = user.getNotifications();
        return notifications.size();
    }

    public void createNotification(UserEntity user, int typeId, String message) {
        if (typeId < 1 || typeId > 5) {
            throw new IncorrectNotificationType();
        }
        NotificationEntity notification = new NotificationEntity(
                user,
                typeId,
                message
        );
        notificationRepository.save(notification);
    }

    public GetNotificationsResponse getNotifications(UserEntity user) {
        return new GetNotificationsResponse(user.getNotifications());
    }
}
