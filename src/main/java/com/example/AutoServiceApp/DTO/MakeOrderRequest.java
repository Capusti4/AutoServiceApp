package com.example.AutoServiceApp.DTO;

import java.math.BigDecimal;

public record MakeOrderRequest(
        Long typeId,
        String comment,
        BigDecimal budget
) {
}
