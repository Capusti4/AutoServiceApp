package com.example.AutoServiceApp.Entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_types")
public class OrderTypeEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, unique = true)
    private String name;

    protected OrderTypeEntity() {}

}


