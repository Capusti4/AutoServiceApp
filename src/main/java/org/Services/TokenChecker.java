package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;

public class TokenChecker {
    public static Document GetUserData(String username, String sessionToken, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = GetCollection(mongoClient, requestURI);
        Document found = usersCollection.find(new Document("username", username)).first();
        mongoClient.close();
        if (found != null) {
            for (Object token : found.get("sessionTokens", ArrayList.class)) {
                if (sessionToken.equals(token)) {
                    found.remove("sessionTokens");
                    found.remove("password");
                    return new Document("userData", found).append("sessionToken", token);
                }
            }
        }
        return null;
    }
}
