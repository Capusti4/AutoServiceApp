package org.Services;
import org.Exceptions.IncorrectSessionToken;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Map;

import static org.Services.ServiceFunctions.getUserDocument;

public class TokenChecker {
    public static Document getUserData(String username, String sessionToken, String requestURI) throws Exception {
        Document found = getUserDocument(username, requestURI);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                found.remove("sessionTokens");
                found.remove("password");
                return new Document("userData", found).append("sessionToken", token);
            }
        }
        throw new IncorrectSessionToken();
    }

    public static void checkUserToken(Map<String, Object> data, String requestURI) throws Exception {
        String username = (String) data.get("username");
        String sessionToken = (String) data.get("sessionToken");
        Document found = getUserDocument(username, requestURI);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                return;
            }
        }
        throw new IncorrectSessionToken();
    }
}
