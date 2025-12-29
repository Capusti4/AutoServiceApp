package com.example.AutoServiceApp.DTO;

public record LoginResponse(
        String answer,
        UserDTO user,
        SessionDTO session
) {
}
