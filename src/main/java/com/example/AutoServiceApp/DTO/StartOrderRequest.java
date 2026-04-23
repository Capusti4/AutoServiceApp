package com.example.AutoServiceApp.DTO;

import java.math.BigDecimal;

public record StartOrderRequest(
        BigDecimal price
) {
}
