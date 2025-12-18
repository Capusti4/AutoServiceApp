package com.example.AutoServiceApp.DTO;

import org.bson.types.ObjectId;

public record OrderDTO(
        ObjectId id,
        ObjectId customerId,
        int typeId,
        String type,
        String comment,
        ObjectId workerId,
        boolean hasCustomerFeedback,
        boolean hasWorkerFeedback
) {
}
