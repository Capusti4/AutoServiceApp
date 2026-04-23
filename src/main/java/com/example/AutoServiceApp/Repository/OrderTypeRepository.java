package com.example.AutoServiceApp.Repository;
import com.example.AutoServiceApp.Entity.OrderTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepository extends JpaRepository<OrderTypeEntity, Long> {
}