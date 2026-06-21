package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.Exception.IncorrectName;
import com.example.AutoServiceApp.Exception.IncorrectPhoneNumber;
import com.example.AutoServiceApp.Exception.IncorrectUsername;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


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
    private long id;

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

    @Setter
    @Getter
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

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
}

