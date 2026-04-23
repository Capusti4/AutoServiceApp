package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.GetNotificationsResponse;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @DeleteMapping("/deleteNotification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable UUID notificationId,
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        notificationService.deleteNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление успешно удалено"));
    }

    @GetMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        GetNotificationsResponse response = notificationService.getNotifications(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/getNotificationsAmount")
    public ResponseEntity<?> getNotificationsAmount(
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        int amount = notificationService.getNotificationsAmount(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("amount", amount));
    }

    @PatchMapping("/readNotification/{notificationId}")
    public ResponseEntity<?> readNotification(
            @PathVariable UUID notificationId,
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        notificationService.readNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление прочитано"));
    }

    @PatchMapping("/unreadNotification/{notificationId}")
    public ResponseEntity<?> unreadNotification(
            @PathVariable UUID notificationId,
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        notificationService.unreadNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление отмечено непрочитанным"));
    }

    @PatchMapping("/readAllNotifications")
    public ResponseEntity<?> readAllNotifications(
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        notificationService.readAllNotifications(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Все уведомления успешно прочитаны"));
    }
}