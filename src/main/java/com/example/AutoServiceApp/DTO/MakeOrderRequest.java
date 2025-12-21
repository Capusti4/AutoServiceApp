package com.example.AutoServiceApp.DTO;

public record MakeOrderRequest(
        String username,
        String sessionToken,
        int orderTypeId,
        String orderType,
        String comment
) implements WithUserDataDTO {
}
