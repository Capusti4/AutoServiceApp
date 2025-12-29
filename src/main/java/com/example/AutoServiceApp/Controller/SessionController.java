package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDataDTO;
import com.example.AutoServiceApp.Service.ServiceFunctions;
import com.example.AutoServiceApp.Service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/{userType}/getUser")
    public ResponseEntity<?> getUser(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserDataDTO response = sessionService.getUserData(session);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{userType}/deleteSession/{sessionToken}")
    public ResponseEntity<?> deleteSession(
            @PathVariable String userType,
            @PathVariable String sessionToken,
            @RequestHeader("Username") String username
    ) {
        ServiceFunctions.checkUserType(userType);
        SessionDTO session = new SessionDTO(username, sessionToken);
        sessionService.deleteSessionToken(session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Сессия успешно удалена"));
    }
}
