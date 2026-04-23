package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.LoginOrRegisterResponse;
import com.example.AutoServiceApp.DTO.LoginRequest;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Service.LoginService;
import com.example.AutoServiceApp.Service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {
    private final LoginService loginService;
    private final SessionService sessionService;

    public LoginController(LoginService loginService, SessionService sessionService) {
        this.loginService = loginService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {
        UserDTO user = loginService.login(request);
        session.setAttribute("user", user);
        session.setAttribute("isWorker", user.isWorker());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoginOrRegisterResponse("Успешный вход"));
    }

    @GetMapping("/getMe")
    public UserDTO getMe(
            HttpSession session
    ) {
        return sessionService.getUser(session);
    }
}
