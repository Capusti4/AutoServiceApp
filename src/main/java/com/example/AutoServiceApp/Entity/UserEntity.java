package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.Exception.IncorrectName;
import com.example.AutoServiceApp.Exception.IncorrectPhoneNumber;
import com.example.AutoServiceApp.Exception.IncorrectUsername;
import jakarta.persistence.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_worker")
    private boolean isWorker;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<NotificationEntity> notifications;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<FeedbackEntity> feedbacksByUser;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    private List<FeedbackEntity> feedbacksForUser;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_session_tokens",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "session_token")
    private List<String> sessionTokens;

    protected UserEntity() {
    }

    public UserEntity(String username, String password, String firstName, String lastName, String phoneNumber, boolean isWorker) {
        if (IsGoodName(firstName) && IsGoodName(lastName)) {
            this.firstName = firstName;
            this.lastName = lastName;
        } else {
            throw new IncorrectName();
        }

        if (phoneNumber.length() == 11 && phoneNumber.startsWith("8")) {
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
        sessionTokens = new ArrayList<>();
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

    public String generateSessionToken() {
        String sessionToken = UUID.randomUUID().toString();
        sessionTokens.add(sessionToken);
        return sessionToken;
    }

    public boolean checkSessionToken(String sessionToken) {
        return sessionTokens.contains(sessionToken);
    }

    public void deleteSessionToken(String sessionToken) {
        sessionTokens.remove(sessionToken);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isWorker() {
        return isWorker;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public UUID getId() {
        return id;
    }

    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    public List<FeedbackEntity> getFeedbacksByUser() {
        return feedbacksByUser;
    }

    public List<FeedbackEntity> getFeedbacksForUser() {
        return feedbacksForUser;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }
}

