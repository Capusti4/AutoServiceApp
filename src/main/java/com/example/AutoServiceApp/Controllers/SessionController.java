package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDataDTO;
import com.example.AutoServiceApp.Services.TokensService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SessionController {
    @GetMapping("/{userType}/getUser")
    public ResponseEntity<?> getUser(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        UserDataDTO response = TokensService.getUserData(user, userType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{userType}/deleteSession/{sessionToken}")
    public ResponseEntity<?> deleteSession(
            @PathVariable String userType,
            @PathVariable String sessionToken,
            @RequestHeader("Username") String username
    ) {
        TokensService.deleteSessionToken(username, sessionToken, userType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Сессия успешно удалена"));
    }
}
