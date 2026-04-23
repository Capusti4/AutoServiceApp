package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.OrderTypeEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Repository.OrderRepository;
import com.example.AutoServiceApp.Repository.OrderTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTypeRepository orderTypeRepository;

    public OrderService(OrderRepository orderRepository, OrderTypeRepository orderTypeRepository) {
        this.orderRepository = orderRepository;
        this.orderTypeRepository = orderTypeRepository;
    }

    public void createOrder(MakeOrderRequest request, UserEntity client) {
        OrderTypeEntity type = orderTypeRepository.findById(request.typeId())
                .orElseThrow(IncorrectOrderId::new);
        orderRepository.save(new OrderEntity(type, request, client));
    }

    @Transactional
    public void startOrder(UUID orderId, UserEntity worker, BigDecimal price) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(IncorrectOrderId::new);
        order.start(worker, price);
    }

    @Transactional
    public void completeOrder(UUID orderId, UserEntity worker, BigDecimal price) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(IncorrectOrderId::new);
        order.complete(worker, price);
    }
}
