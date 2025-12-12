package org.Services;

import org.Exceptions.IncorrectSessionToken;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Map;

public class UserIdGiver {
    public static ObjectId getUserId(Map<String, Object> data, String requestURI) throws Exception {
        String username = (String) data.get("username");
        String sessionToken = (String) data.get("sessionToken");
        Document found = ServiceFunctions.getUserDocument(username, requestURI);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (token.equals(sessionToken)) {
                return (ObjectId) found.get("_id");
            }
        }
        throw new IncorrectSessionToken();
    }
}
