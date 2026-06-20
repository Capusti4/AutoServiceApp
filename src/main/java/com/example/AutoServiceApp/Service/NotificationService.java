package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.GetNotificationsResponse;
import com.example.AutoServiceApp.Entity.NotificationEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectNotificationId;
import com.example.AutoServiceApp.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void deleteNotification(UserEntity user, long notificationId) {
        List<NotificationEntity> notifications = notificationRepository.findByUser(user);
        for (NotificationEntity notification : notifications) {
            if (notification.getId() == notificationId) {
                notificationRepository.delete(notification);
                return;
            }
        }
        throw new IncorrectNotificationId();
    }

    public void readNotification(UserEntity user, long notificationId) {
        NotificationEntity notification = getNotification(user, notificationId);
        notification.read();
        notificationRepository.save(notification);
    }

    public void unreadNotification(UserEntity user, long notificationId) {
        NotificationEntity notification = getNotification(user, notificationId);
        notification.unread();
        notificationRepository.save(notification);
    }

    private NotificationEntity getNotification(UserEntity user, long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId).orElseThrow(IncorrectNotificationId::new);
        if (notification.getUserId() != user.getId()) {
            throw new IncorrectNotificationId();
        }
        return notification;
    }

    public void readAllNotifications(UserEntity user) {
        List<NotificationEntity> notifications = notificationRepository.findByUser(user);
        for (NotificationEntity notification : notifications) {
            notification.read();
        }
        notificationRepository.saveAll(notifications);
    }

    public int getUnreadNotificationsAmount(UserEntity user) {
        List<NotificationEntity> notifications = notificationRepository.findByUser(user).stream()
                .filter(n -> !n.isRead())
                .toList();
        return notifications.size();
    }

    public void createNotification(UserEntity user, String message) {
        NotificationEntity notification = new NotificationEntity(
                user,
                message
        );
        notificationRepository.save(notification);
    }

    public GetNotificationsResponse getNotifications(UserEntity user) {
        return new GetNotificationsResponse(notificationRepository.findByUser(user));
    }
}
