package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.AppException;
import com.example.AutoServiceApp.Exception.IncorrectNotificationType;
import com.example.AutoServiceApp.Repository.OrderRepository;
import com.example.AutoServiceApp.Service.*;
import jakarta.servlet.http.HttpSession;
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
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository, NotificationService notificationService) {
        this.orderService = orderService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
    }

    @PatchMapping("/startOrder/{orderId}")
    public ResponseEntity<?> startOrder(
            @PathVariable UUID orderId,
            @RequestBody StartOrderRequest request,
            HttpSession session
    ) {
        UserEntity worker = userService.getWorker(session);
        orderService.startOrder(orderId, worker, request.price());
        sendNotificationByOrder(orderId, 1);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно взят в работу"));
    }

    @PatchMapping("/completeOrder/{orderId}")
    public ResponseEntity<?> completeOrder(
            @PathVariable UUID orderId,
            @RequestBody StartOrderRequest request,
            HttpSession session
    ) {
        UserEntity worker = userService.getWorker(session);
        orderService.completeOrder(orderId, worker, request.price());
        sendNotificationByOrder(orderId, 2);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));
    }

    private void sendNotificationByOrder(UUID orderId, int notificationType) {
        UserEntity user = orderRepository.findById(orderId)
                .map(OrderEntity::getCustomer)
                .orElseThrow(() -> new AppException("Некорректный заказ", 400));
        switch (notificationType) {
            case 1:
                notificationService.createNotification(user, notificationType, "Ваш заказ №" + orderId + "взят в работу!");
                break;
            case 2:
                notificationService.createNotification(user, notificationType, "Ваш заказ №" + orderId + "завершен!");
                break;
            default:
                throw new IncorrectNotificationType();
        }
    }

    @PostMapping("/makeOrder")
    public ResponseEntity<?> makeOrder(
            @RequestBody MakeOrderRequest request,
            HttpSession session
    ) {
        UserEntity client = userService.getClient(session);
        orderService.createOrder(request, client);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Заказ успешно создан"));
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        GetOrdersResponse response;
        if (user.isWorker()) {
            List<OrderDTO> workerOrders = new ArrayList<>();
            workerOrders.addAll(orderRepository.findAllByWorker(user));
            workerOrders.addAll(orderRepository.findAllByStatus("new"));
            response = new GetOrdersResponse(workerOrders);
        } else {
            response = new GetOrdersResponse(user.getOrders());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
