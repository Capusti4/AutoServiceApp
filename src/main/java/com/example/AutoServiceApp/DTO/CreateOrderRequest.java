package com.example.AutoServiceApp.DTO;

public record CreateOrderRequest(
        String username,
        String sessionToken,
        int orderTypeId,
        String orderType,
        String comment
) implements WithUserDataDTO {
}
