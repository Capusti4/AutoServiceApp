package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.Services.*;
import com.example.AutoServiceApp.Exceptions.AppException;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class NotificationController {
    @PostMapping("/{userType}/deleteNotification")
    public ResponseEntity<?> deleteNotification(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            TokenChecker.checkUserToken(data, userType);
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            NotificationDeleter.deleteNotification(notificationId);
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

    @PostMapping("/{userType}/getNotifications")
    public ResponseEntity<?> getNotifications(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            ObjectId userId = UserIdGiver.getUserId(data, userType);
            String notificationsJson = NotificationsGiver.getNotifications(userId);
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

    @PostMapping("/{userType}/getNotificationsAmount")
    public ResponseEntity<?> getNotificationsAmount(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            ObjectId userId = UserIdGiver.getUserId(data, userType);
            int amount = NotificationsAmountGiver.getNotificationsAmount(userId);
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

    @PostMapping("/{userType}/readNotification")
    public ResponseEntity<?> readNotification(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            TokenChecker.checkUserToken(data, userType);
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            NotificationReader.readNotification(notificationId);
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

    @PostMapping("/{userType}/readAllNotifications")
    public ResponseEntity<?> readAllNotifications(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            ObjectId userId = UserIdGiver.getUserId(data, userType);
            NotificationReader.readAllNotifications(userId);
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