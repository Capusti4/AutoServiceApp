package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.RegisterResponse;
import com.example.AutoServiceApp.DTO.RegistrationRequest;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Service.RegistrationService;
import com.example.AutoServiceApp.Service.ServiceFunctions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    protected final RegistrationService registrationService;


    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{userType}/register")
    public ResponseEntity<?> registerUser(
            @PathVariable String userType,
            @RequestBody RegistrationRequest request
    ) {
        ServiceFunctions.checkUserType(userType);
        boolean isWorker = userType.equalsIgnoreCase("worker");
        UserEntity user = new UserEntity(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                isWorker
        );
        RegisterResponse response = registrationService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
