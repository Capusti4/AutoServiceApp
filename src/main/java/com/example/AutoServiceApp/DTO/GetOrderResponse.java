package com.example.AutoServiceApp.DTO;

import java.util.List;

public record GetOrderResponse(
        List<OrderDTO> orders
) {
}
