package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.WithUserDataDTO;
import com.example.AutoServiceApp.Exceptions.IncorrectSessionToken;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class UserIdService {
    public static ObjectId getUserId(WithUserDataDTO request, String userType) {
        String username = request.username();
        String sessionToken = request.sessionToken();
        Document found = ServiceFunctions.getUserDocument(username, userType);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (token.equals(sessionToken)) {
                return (ObjectId) found.get("_id");
            }
        }
        throw new IncorrectSessionToken();
    }
}
