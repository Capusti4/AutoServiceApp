package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.GetNotificationsResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Service.*;
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

    @DeleteMapping("/{userType}/deleteNotification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable String userType,
            @PathVariable UUID notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        notificationService.deleteNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление успешно удалено"));
    }

    @GetMapping("/{userType}/getNotifications")
    public ResponseEntity<?> getNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        GetNotificationsResponse response = notificationService.getNotifications(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{userType}/getNotificationsAmount")
    public ResponseEntity<?> getNotificationsAmount(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        int amount = notificationService.getNotificationsAmount(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("amount", amount));
    }

    @PatchMapping("/{userType}/readNotification/{notificationId}")
    public ResponseEntity<?> readNotification(
            @PathVariable String userType,
            @PathVariable UUID notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        notificationService.readNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление прочитано"));
    }

    @PatchMapping("/{userType}/unreadNotification/{notificationId}")
    public ResponseEntity<?> unreadNotification(
            @PathVariable String userType,
            @PathVariable UUID notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        notificationService.unreadNotification(user, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление отмечено непрочитанным"));
    }

    @PatchMapping("/{userType}/readAllNotifications")
    public ResponseEntity<?> readAllNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        notificationService.readAllNotifications(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Все уведомления успешно прочитаны"));
    }
}