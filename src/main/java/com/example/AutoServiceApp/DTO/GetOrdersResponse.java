package com.example.AutoServiceApp.DTO;

import java.util.List;

public record GetOrdersResponse(
        List<OrderDTO> orders
) {
}
