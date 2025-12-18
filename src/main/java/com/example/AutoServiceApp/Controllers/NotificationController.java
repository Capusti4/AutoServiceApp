package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Services.*;
import com.example.AutoServiceApp.Exceptions.AppException;
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
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            TokensService.checkUserToken(user, userType);
            NotificationsService.deleteNotification(notificationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Уведомление успешно удалено"));
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    @GetMapping("/{userType}/getNotifications")
    public ResponseEntity<?> getNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            ObjectId userId = UserIdService.getUserId(user, userType);
            String notificationsJson = NotificationsService.getNotifications(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationsJson);
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    @GetMapping("/{userType}/getNotificationsAmount")
    public ResponseEntity<?> getNotificationsAmount(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            ObjectId userId = UserIdService.getUserId(user, userType);
            int amount = NotificationsService.getNotificationsAmount(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(amount);
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    @PatchMapping("/{userType}/readNotification/{notificationId}")
    public ResponseEntity<?> readNotification(
            @PathVariable String userType,
            @PathVariable ObjectId notificationId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            TokensService.checkUserToken(user, userType);
            NotificationsService.readNotification(notificationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Сообщение прочитано"));
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    @PatchMapping("/{userType}/readAllNotifications")
    public ResponseEntity<?> readAllNotifications(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            ObjectId userId = UserIdService.getUserId(user, userType);
            NotificationsService.readAllNotifications(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Все уведомления успешно прочитаны"));
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }
}