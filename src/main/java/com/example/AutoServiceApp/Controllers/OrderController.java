package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.CreateOrderRequest;
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

    @PostMapping("/client/createOrder")
    public ResponseEntity<?> createOrder(
            @RequestBody CreateOrderRequest request
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

    @GetMapping("/worker/getNewOrders")
    public ResponseEntity<?> getNewOrders(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, "worker");
        String newOrdersJson = OrdersService.getNewOrdersList();
        return ResponseEntity.status(HttpStatus.OK).body(newOrdersJson);
    }

    @GetMapping("/worker/getActiveOrders")
    public ResponseEntity<?> getActiveOrders(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, "worker");
        String activeOrdersJson = OrdersService.getActiveOrders();
        return ResponseEntity.status(HttpStatus.OK).body(activeOrdersJson);
    }

    @GetMapping("/worker/getCompletedOrders")
    public ResponseEntity<?> getCompletedOrders(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        TokensService.checkUserToken(user, "worker");
        String completedOrdersJson = OrdersService.getCompletedOrders();
        return ResponseEntity.status(HttpStatus.OK).body(completedOrdersJson);
    }
}
