package com.example.AutoServiceApp.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        UUID workerId,
        BigDecimal price,
        BigDecimal budget,
        String type,
        String status,
        String comment,
        boolean hasCustomerFeedback,
        boolean hasWorkerFeedback
) {
}
