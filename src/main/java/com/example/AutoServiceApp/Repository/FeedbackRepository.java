package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {
}
