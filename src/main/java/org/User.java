package org;

import org.Exceptions.IncorrectName;
import org.Exceptions.IncorrectPhoneNumber;

public class User {
    private final String Username;
    private final String FirstName;
    private final String LastName;
    private final String PhoneNum;
    private final String password;

    public User(String username, String password, String firstName, String lastName, String phoneNum) {
        if (IsGoodName(firstName) && IsGoodName(lastName)){
            this.FirstName = firstName;
            this.LastName = lastName;
        } else{
            throw new IncorrectName();
        }

        if (phoneNum.length() == 11 && phoneNum.startsWith("8")){
            for (char c : phoneNum.toCharArray()){
                if (!Character.isDigit(c)){
                    throw new IncorrectPhoneNumber();
                }
            }
        } else {
            throw new IncorrectPhoneNumber();
        }
        this.PhoneNum = phoneNum;
        this.Username = username;
        this.password = password;
    }

    private boolean IsGoodName(String name){
        for (char c : name.toCharArray()){
            if (!Character.isLetter(c)){
                return false;
            }
        }
        return true;
    }

    public String getUsername() {
        return Username;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public String getPhoneNum() {
        return PhoneNum;
    }
    public String getPassword() {
        return password;
    }
}