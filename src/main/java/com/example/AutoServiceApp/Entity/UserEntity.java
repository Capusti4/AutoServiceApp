package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.DTO.OrderDTO;
import com.example.AutoServiceApp.Exception.IncorrectName;
import com.example.AutoServiceApp.Exception.IncorrectPhoneNumber;
import com.example.AutoServiceApp.Exception.IncorrectUsername;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone_number")
        }
)
public class UserEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    @Column(name = "is_worker")
    private boolean isWorker;

    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Getter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Getter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Getter
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<NotificationEntity> notifications;

    @Getter
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<FeedbackEntity> feedbacksByUser;

    @Getter
    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    private List<FeedbackEntity> feedbacksForUser;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    protected UserEntity() {
    }

    public UserEntity(String username, String password, String firstName, String lastName, String phoneNumber, boolean isWorker) {
        if (IsGoodName(firstName) && IsGoodName(lastName)) {
            this.firstName = firstName;
            this.lastName = lastName;
        } else {
            throw new IncorrectName();
        }

        if (phoneNumber.length() == 11 && (phoneNumber.startsWith("8") || phoneNumber.startsWith("+7"))) {
            for (char c : phoneNumber.toCharArray()) {
                if (!Character.isDigit(c)) {
                    throw new IncorrectPhoneNumber();
                }
            }
        } else {
            throw new IncorrectPhoneNumber();
        }
        this.phoneNumber = phoneNumber;
        if (!Character.isLetter(username.charAt(0)) || !username.matches("[A-Za-z0-9]+")) {
            throw new IncorrectUsername();
        }
        this.username = username;
        this.password = password;
        orders = new ArrayList<>();
        notifications = new ArrayList<>();
        feedbacksByUser = new ArrayList<>();
        feedbacksForUser = new ArrayList<>();
        this.isWorker = isWorker;
    }

    private boolean IsGoodName(String name) {
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Transactional
    public List<OrderDTO> getOrders() {
        List<OrderDTO> orders = new ArrayList<>();
        for (OrderEntity order : this.orders) {
            orders.add(order.getDTO());
        }
        return orders;
    }
}

