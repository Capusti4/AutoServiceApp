package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.Objects.Order;
import com.example.AutoServiceApp.Services.*;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {
    @PostMapping("/worker/startOrder")
    public ResponseEntity<?> startOrder(@RequestBody Map<String, Object> data) {
        ObjectId workerId = UserIdGiver.getUserId(data, "worker");
        ObjectId orderId = new ObjectId((String) data.get("orderId"));
        OrderStarter.startOrder(orderId, workerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно взят в работу"));
    }

    @PostMapping("/worker/completeOrder")
    public ResponseEntity<?> completeOrder(@RequestBody Map<String, Object> data) {
        ObjectId workerId = UserIdGiver.getUserId(data, "worker");
        ObjectId orderId = new ObjectId((String) data.get("orderId"));
        OrderCompleter.completeOrder(orderId, workerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Заказ успешно завершен"));

    }

    @PostMapping("/client/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        int orderTypeId = Math.toIntExact(Math.round((double) data.get("orderTypeId")));
        String comment = (String) data.get("comment");
        Map<String, Object> userInfo = (Map<String, Object>) data.get("userSessionInfo");
        ObjectId userId = UserIdGiver.getUserId(userInfo, "client");
        Order order;
        if (orderTypeId == 0) {
            order = new Order((String) data.get("orderType"), userId, comment);
        } else {
            order = new Order(orderTypeId, userId, comment);
        }
        OrderCreator.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Заказ успешно создан"));
    }


    @PostMapping("/worker/getNewOrders")
    public ResponseEntity<?> getNewOrders(@RequestBody Map<String, Object> data) {
        TokenChecker.checkUserToken(data, "worker");
        String newOrdersJson = NewOrdersGiver.getNewOrdersList();
        return ResponseEntity.status(HttpStatus.OK).body(newOrdersJson);
    }


    @PostMapping("/worker/getActiveOrders")
    public ResponseEntity<?> getActiveOrders(@RequestBody Map<String, Object> data) {
        TokenChecker.checkUserToken(data, "worker");
        String activeOrdersJson = ActiveOrdersGiver.getActiveOrders();
        return ResponseEntity.status(HttpStatus.OK).body(activeOrdersJson);

    }

    @PostMapping("/worker/getCompletedOrders")
    public ResponseEntity<?> getCompletedOrders(@RequestBody Map<String, Object> data) {
        TokenChecker.checkUserToken(data, "worker");
        String completedOrdersJson = CompletedOrdersGiver.getCompletedOrders();
        return ResponseEntity.status(HttpStatus.OK).body(completedOrdersJson);
    }
}
