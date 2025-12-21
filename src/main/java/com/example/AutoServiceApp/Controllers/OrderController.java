package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.GetOrdersResponse;
import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Objects.Order;
import com.example.AutoServiceApp.Services.*;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderController {
    @PatchMapping("/worker/startOrder/{orderId}")
    public ResponseEntity<?> startOrder(
            @PathVariable ObjectId orderId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId workerId = UserIdService.getUserId(user, "worker");
        OrdersService.startOrder(orderId, workerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно взят в работу"));
    }

    @PatchMapping("/worker/completeOrder/{orderId}")
    public ResponseEntity<?> completeOrder(
            @PathVariable ObjectId orderId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId workerId = UserIdService.getUserId(user, "worker");
        OrdersService.completeOrder(orderId, workerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));

    }

    @PostMapping("/client/makeOrder")
    public ResponseEntity<?> makeOrder(
            @RequestBody MakeOrderRequest request
    ) {
        ObjectId userId = UserIdService.getUserId(request, "client");
        Order order;
        if (request.orderTypeId() == 0) {
            order = new Order(request.orderType(), userId, request.comment());
        } else {
            order = new Order(request.orderTypeId(), userId, request.comment());
        }
        OrdersService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Заказ успешно создан"));
    }

    @GetMapping("/{userType}/getOrders")
    public ResponseEntity<?> getClientOrders(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        GetOrdersResponse response = OrdersService.getUserOrders(userId, userType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
