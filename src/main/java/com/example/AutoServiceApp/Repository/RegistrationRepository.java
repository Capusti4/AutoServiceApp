package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<UserEntity, UUID> {
}
