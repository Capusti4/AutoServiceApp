package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.RegisterResponse;
import com.example.AutoServiceApp.DTO.RegistrationRequest;
import com.example.AutoServiceApp.Services.RegistrationService;
import com.example.AutoServiceApp.Objects.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    @PostMapping("/{userType}/register")
    public ResponseEntity<?> registerUser(
            @PathVariable String userType,
            @RequestBody RegistrationRequest request
    ) {
        User user = createUser(request);
        RegisterResponse response = RegistrationService.register(user, userType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    static User createUser(RegistrationRequest request) {
        return new User(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber()
        );
    }
}
