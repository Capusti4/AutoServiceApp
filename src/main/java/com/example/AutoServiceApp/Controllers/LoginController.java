package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.LoginRequest;
import com.example.AutoServiceApp.DTO.LoginResponse;
import com.example.AutoServiceApp.Services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
    @PostMapping("/{userType}/login")
    public ResponseEntity<?> login(
            @PathVariable String userType,
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = LoginService.login(request, userType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
