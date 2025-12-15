package com.example.AutoServiceApp.Services;
import com.example.AutoServiceApp.Exceptions.IncorrectSessionToken;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Map;

import static com.example.AutoServiceApp.Services.ServiceFunctions.getUserDocument;

public class TokenChecker {
    public static Document getUserData(String username, String sessionToken, String userType) {
        Document found = getUserDocument(username, userType);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                found.remove("sessionTokens");
                found.remove("password");
                return new Document("userData", found).append("sessionToken", token);
            }
        }
        throw new IncorrectSessionToken();
    }

    public static void checkUserToken(Map<String, Object> data, String userType) {
        String username = (String) data.get("username");
        String sessionToken = (String) data.get("sessionToken");
        Document found = getUserDocument(username, userType);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                return;
            }
        }
        throw new IncorrectSessionToken();
    }
}
