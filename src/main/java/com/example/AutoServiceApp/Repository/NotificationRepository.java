package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.NotificationEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser(UserEntity user);
}
