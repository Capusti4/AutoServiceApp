package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.Exception.IncorrectUserType;


public class ServiceFunctions {
    public static void checkUserType(String userType) {
        if (!userType.equalsIgnoreCase("client") && !userType.equalsIgnoreCase("worker")) {
            throw new IncorrectUserType();
        }
    }
}
