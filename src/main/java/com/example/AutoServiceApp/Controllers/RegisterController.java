package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.RegisterResponse;
import com.example.AutoServiceApp.DTO.RegistrationRequest;
import com.example.AutoServiceApp.Exceptions.*;
import com.example.AutoServiceApp.Services.RegistrationService;
import com.example.AutoServiceApp.Objects.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterController {

    @PostMapping("/{userType}/register")
    public ResponseEntity<?> registerUser(
            @PathVariable String userType,
            @RequestBody RegistrationRequest request
    ) {
        try {
            User user = createUser(request);
            RegisterResponse response = RegistrationService.register(user, userType);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AppException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Неизвестная ошибка", "details", e.getMessage()));
        }
    }

    static User createUser(RegistrationRequest request) {
        return new User(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNum()
        );
    }
}
