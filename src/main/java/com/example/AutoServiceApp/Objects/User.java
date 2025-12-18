package com.example.AutoServiceApp.Objects;

import com.example.AutoServiceApp.Exceptions.IncorrectName;
import com.example.AutoServiceApp.Exceptions.IncorrectPhoneNumber;

public record User(String username, String password, String firstName, String lastName, String phoneNumber) {
    public User(String username, String password, String firstName, String lastName, String phoneNumber) {
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
        this.username = username;
        this.password = password;
    }

    private boolean IsGoodName(String name) {
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}