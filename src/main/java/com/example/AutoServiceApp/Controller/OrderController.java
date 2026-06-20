package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.OrderTypeEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectNotificationType;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Repository.OrderRepository;
import com.example.AutoServiceApp.Repository.OrderTypeRepository;
import com.example.AutoServiceApp.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final OrderTypeRepository orderTypeRepository;

    public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository, NotificationService notificationService, OrderTypeRepository orderTypeRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
        this.orderTypeRepository = orderTypeRepository;
    }

    @PatchMapping("/startOrder/{orderId}")
    public ResponseEntity<?> startOrder(
            @PathVariable long orderId,
            @RequestBody StartOrderRequest request,
            HttpSession session
    ) {
        UserEntity worker = userService.getWorker(session);
        orderService.startOrder(orderId, worker, request.price());
        sendNotificationByOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно взят в работу"));
    }

    @PatchMapping("/completeOrder/{orderId}")
    public ResponseEntity<?> completeOrder(
            @PathVariable long orderId,
            @RequestBody StartOrderRequest request,
            HttpSession session
    ) {
        UserEntity worker = userService.getWorker(session);
        orderService.completeOrder(orderId, worker, request.price());
        sendNotificationByOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));
    }

    private void sendNotificationByOrder(long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(IncorrectOrderId::new);
        UserEntity user = order.getCustomer();
        String price = new DecimalFormat("0.##").format(order.getPrice());
        switch (order.getStatus()) {
            case "active":
                notificationService.createNotification(user,
                        "Ваш заказ №" + orderId + " взят в работу! Предварительная цена за заказ: " + price + " руб.");
                break;
            case "completed":
                notificationService.createNotification(user,
                        "Ваш заказ №" + orderId + " завершен! Цена за заказ: " + price + " руб.");
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

    @GetMapping("/getOrderTypes")
    public ResponseEntity<?> getOrderTypes(
            HttpSession session
    ) {
        userService.getUser(session);
        List<OrderTypeEntity> types = orderTypeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(types);
    }
}
