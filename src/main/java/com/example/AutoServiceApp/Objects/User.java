package com.example.AutoServiceApp.Objects;

import com.example.AutoServiceApp.Exceptions.IncorrectName;
import com.example.AutoServiceApp.Exceptions.IncorrectPhoneNumber;

public record User(String username, String password, String firstName, String lastName, String phoneNum) {
    public User(String username, String password, String firstName, String lastName, String phoneNum) {
        if (IsGoodName(firstName) && IsGoodName(lastName)) {
            this.firstName = firstName;
            this.lastName = lastName;
        } else {
            throw new IncorrectName();
        }

        if (phoneNum.length() == 11 && phoneNum.startsWith("8")) {
            for (char c : phoneNum.toCharArray()) {
                if (!Character.isDigit(c)) {
                    throw new IncorrectPhoneNumber();
                }
            }
        } else {
            throw new IncorrectPhoneNumber();
        }
        this.phoneNum = phoneNum;
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