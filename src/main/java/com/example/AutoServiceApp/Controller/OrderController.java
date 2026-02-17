package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.GetOrdersResponse;
import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectUserType;
import com.example.AutoServiceApp.Repository.OrderRepository;
import com.example.AutoServiceApp.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @PatchMapping("/worker/startOrder/{orderId}")
    public ResponseEntity<?> startOrder(
            @PathVariable UUID orderId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        UserEntity worker = userService.getUser(user);
        orderService.startOrder(orderId, worker);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно взят в работу"));
    }

    @PatchMapping("/worker/completeOrder/{orderId}")
    public ResponseEntity<?> completeOrder(
            @PathVariable UUID orderId,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO user = new SessionDTO(username, sessionToken);
        UserEntity worker = userService.getUser(user);
        orderService.completeOrder(orderId, worker);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));

    }

    @PostMapping("/client/makeOrder")
    public ResponseEntity<?> makeOrder(
            @RequestBody MakeOrderRequest request
    ) {
        UserEntity client = userService.getClient(request);
        OrderEntity order = new OrderEntity(request, client);
        orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Заказ успешно создан"));
    }

    @GetMapping("/client/getOrders")
    public ResponseEntity<?> getClientOrders(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        if (user.isWorker()) {
            throw new IncorrectUserType();
        }
        GetOrdersResponse response = new GetOrdersResponse(user.getOrders());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/worker/getOrders")
    public ResponseEntity<?> getWorkerOrders(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        SessionDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        if (!user.isWorker()) {
            throw new IncorrectUserType();
        }
        List<OrderEntity> workerOrders = new ArrayList<>();
        workerOrders.addAll(user.getOrders());
        workerOrders.addAll(orderRepository.findAllByStatus("new"));
        GetOrdersResponse response = new GetOrdersResponse(workerOrders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
