package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
