package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Services.TokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.AutoServiceApp.Exceptions.AppException;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SessionController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/{userType}/getUser")
    public ResponseEntity<?> getUser(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        try {
            SessionDTO user = new SessionDTO(username, sessionToken);
            Document userData = TokensService.getUserData(user, userType);
            Map<String, Object> responseBody = Map.of(
                    "userData", objectMapper.readValue(userData.toJson(), Map.class)
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

    @PatchMapping("/{userType}/deleteSession/{sessionToken}")
    public ResponseEntity<?> deleteSession(
            @PathVariable String userType,
            @PathVariable String sessionToken,
            @RequestHeader("Username") String username
    ) {
        try {
            TokensService.deleteSessionToken(username, sessionToken, userType);
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
