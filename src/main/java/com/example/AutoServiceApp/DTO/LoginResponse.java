package com.example.AutoServiceApp.DTO;

public record LoginResponse(
        String answer,
        UserDTO userData,
        SessionDTO sessionInfo
) {
}
