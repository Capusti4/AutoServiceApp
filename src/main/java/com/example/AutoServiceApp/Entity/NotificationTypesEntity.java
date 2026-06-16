package com.example.AutoServiceApp.Entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "notification_types")
public class NotificationTypesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Getter
    @Column(nullable = false)
    private String name;

    protected NotificationTypesEntity() {}
}
