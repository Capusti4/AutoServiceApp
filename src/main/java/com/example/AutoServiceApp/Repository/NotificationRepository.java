package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
}
