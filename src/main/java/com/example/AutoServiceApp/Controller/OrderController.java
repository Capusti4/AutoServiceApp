package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.GetOrdersResponse;
import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.DTO.OrderDTO;
import com.example.AutoServiceApp.DTO.StartOrderRequest;
import com.example.AutoServiceApp.Entity.UserEntity;
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
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userService = userService;
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
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));
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
