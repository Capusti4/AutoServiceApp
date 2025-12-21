package com.example.AutoServiceApp.DTO;

public record OrderDTO(
        String id,
        String customerId,
        int typeId,
        String type,
        String comment,
        String workerId,
        String status,
        boolean hasCustomerFeedback,
        boolean hasWorkerFeedback
) {
}
