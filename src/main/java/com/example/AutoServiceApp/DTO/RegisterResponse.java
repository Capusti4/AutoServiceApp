package com.example.AutoServiceApp.DTO;

public record RegisterResponse(
        String answer,
        UserDTO user,
        SessionDTO session
) {
}
