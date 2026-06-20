package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
}
