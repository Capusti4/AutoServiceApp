package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.LoginOrRegisterResponse;
import com.example.AutoServiceApp.DTO.RegistrationRequest;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Service.RegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
    protected final RegistrationService registrationService;


    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody RegistrationRequest request,
            HttpSession session
    ) {
        boolean isWorker = request.isWorker();
        UserEntity user = new UserEntity(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                isWorker
        );
        UserDTO userDTO = registrationService.register(user);
        session.setAttribute("user", userDTO);
        session.setAttribute("isWorker", user.isWorker());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginOrRegisterResponse("Успешная регистрация"));
    }
}
