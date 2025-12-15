package com.example.AutoServiceApp.DTO;

public record RegistrationRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String phoneNum
) {
}
