package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(OrderEntity order) {
        orderRepository.save(order);
    }

    public void startOrder(UUID orderId, UserEntity worker) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(IncorrectOrderId::new);
        order.start(worker);
    }

    public void completeOrder(UUID orderId, UserEntity worker) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(IncorrectOrderId::new);
        order.complete(worker);
    }
}
