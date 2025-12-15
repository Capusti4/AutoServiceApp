package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.Services.TokenDeleter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.AutoServiceApp.Exceptions.AppException;
import com.example.AutoServiceApp.Services.TokenChecker;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SessionController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/{userType}/checkSession")
    public ResponseEntity<?> checkSession(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            Document user = TokenChecker.getUserData(username, sessionToken, userType);
            Map<String, Object> responseBody = Map.of(
                    "userData", objectMapper.readValue(user.toJson(), Map.class)
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    @PostMapping("/{userType}/deleteSession")
    public ResponseEntity<?> deleteSession(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        try {
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            TokenDeleter.deleteSessionToken(username, sessionToken, userType);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Сессия успешно удалена"));
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }
}
