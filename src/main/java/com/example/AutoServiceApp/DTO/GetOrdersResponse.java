package com.example.AutoServiceApp.DTO;

import com.example.AutoServiceApp.Entity.OrderEntity;

import java.util.List;

public record GetOrdersResponse(
        List<OrderEntity> orders
) {
}
