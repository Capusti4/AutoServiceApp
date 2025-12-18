package com.example.AutoServiceApp.DTO;

public record UserDataDTO(
    String username,
    String firstName,
    String lastName,
    String phoneNumber,
    SessionDTO session
) {
}
