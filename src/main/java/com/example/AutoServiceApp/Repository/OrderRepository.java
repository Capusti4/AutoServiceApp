package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository
        extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByStatus(String status);

    List<OrderEntity> findAllByWorker(UserEntity worker);

    List<OrderEntity> findAllByCustomer(UserEntity customer);
}
