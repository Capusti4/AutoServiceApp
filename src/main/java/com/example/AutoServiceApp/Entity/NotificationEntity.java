package com.example.AutoServiceApp.Entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Getter
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Getter
    @Column(nullable = false)
    private String message;

    protected NotificationEntity() {
    }

    public NotificationEntity(UserEntity user, String message) {
        this.user = user;
        this.isRead = false;
        this.message = message;
    }

    public void read() {
        isRead = true;
    }

    public void unread() {
        isRead = false;
    }

    public long getUserId() {
        return user.getId();
    }
}
