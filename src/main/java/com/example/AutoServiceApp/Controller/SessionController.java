package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDataDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectUserType;
import com.example.AutoServiceApp.Service.ServiceFunctions;
import com.example.AutoServiceApp.Service.SessionService;
import com.example.AutoServiceApp.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SessionController {
    private final SessionService sessionService;
    private final UserService userService;

    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
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

    @DeleteMapping("/{userType}/deleteSession/{sessionToken}")
    public ResponseEntity<?> deleteSession(
            @PathVariable String userType,
            @PathVariable String sessionToken,
            @RequestHeader("Username") String username
    ) {
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        if ((user.isWorker() && !userType.equals("worker")) || (!user.isWorker() && !userType.equals("client"))) {
            throw new IncorrectUserType();
        }
        sessionService.deleteSessionToken(session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Сессия успешно удалена"));
    }
}
