package com.example.AutoServiceApp.DTO;

public record RegisterResponse(
        String answer,
        UserDTO userData,
        SessionDTO sessionInfo
) {
}
