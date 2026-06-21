package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.FeedbackEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> getByTarget(UserEntity target);

    List<FeedbackEntity> getByAuthor(UserEntity user);
}

