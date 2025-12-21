package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.GetNotificationsResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Services.*;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class NotificationController {
    @DeleteMapping("/{userType}/deleteNotification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable String userType,
            @PathVariable ObjectId notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, userType);
        NotificationsService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление успешно удалено"));
    }

    @GetMapping("/{userType}/getNotifications")
    public ResponseEntity<?> getNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        GetNotificationsResponse response = NotificationsService.getNotifications(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{userType}/getNotificationsAmount")
    public ResponseEntity<?> getNotificationsAmount(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        int amount = NotificationsService.getNotificationsAmount(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("amount", amount));
    }

    @PatchMapping("/{userType}/readNotification/{notificationId}")
    public ResponseEntity<?> readNotification(
            @PathVariable String userType,
            @PathVariable ObjectId notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, userType);
        NotificationsService.readNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление прочитано"));
    }
    @PatchMapping("/{userType}/unreadNotification/{notificationId}")
    public ResponseEntity<?> unreadNotification(
            @PathVariable String userType,
            @PathVariable ObjectId notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, userType);
        NotificationsService.unreadNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Уведомление отмечено непрочитанным"));
    }

    @PatchMapping("/{userType}/readAllNotifications")
    public ResponseEntity<?> readAllNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        NotificationsService.readAllNotifications(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Все уведомления успешно прочитаны"));
    }
}