package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.LoginRequest;
import com.example.AutoServiceApp.DTO.LoginResponse;
import com.example.AutoServiceApp.Service.LoginService;
import com.example.AutoServiceApp.Service.ServiceFunctions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/{userType}/login")
    public ResponseEntity<?> login(
            @PathVariable String userType,
            @RequestBody LoginRequest request
    ) {
        ServiceFunctions.checkUserType(userType);
        LoginResponse response = loginService.login(request, userType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
