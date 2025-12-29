package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<OrderEntity, UUID> {
}
