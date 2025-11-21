package org.Services;
import org.bson.Document;
import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetUserDocument;

public class TokenChecker {
    public static Document GetUserData(String username, String sessionToken, String requestURI) throws Exception {
        Document found = GetUserDocument(username, requestURI);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                found.remove("sessionTokens");
                found.remove("password");
                return new Document("userData", found).append("sessionToken", token);
            }
        }
        return null;
    }
}
