package com.example.AutoServiceApp.DTO;

import com.example.AutoServiceApp.Entity.UserEntity;

import java.math.BigDecimal;


public record OrderDTO(
        long id,
        UserEntity worker,
        BigDecimal price,
        BigDecimal budget,
        String type,
        String status,
        String comment,
        boolean hasCustomerFeedback,
        boolean hasWorkerFeedback
) {
}
