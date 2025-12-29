package com.example.AutoServiceApp.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "type_id", nullable = false)
    private int typeId;

    @Column(nullable = false)
    private String message;

    protected NotificationEntity() {
    }

    public NotificationEntity(UserEntity user, int typeId, String message) {
        this.user = user;
        this.isRead = false;
        this.typeId = typeId;
        this.message = message;
    }

    public void read() {
        isRead = true;
    }

    public void unread() {
        isRead = false;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }
}
