package org.Client.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class TokenChecker {
    public static String CheckToken(String username, String token) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase usersDatabase = mongoClient.getDatabase("Clients");
        MongoCollection<Document> usersCollection = usersDatabase.getCollection("Clients");

        Document found = usersCollection.find(new Document("username", username)).first();
        mongoClient.close();
        if (found != null) {
            for (Object sessionToken : found.get("sessionTokens", ArrayList.class)) {
                if (sessionToken.equals(token)) {
                    found.remove("sessionTokens");
                    found.remove("password");
                    found.remove("_id");
                    Document responseDoc = new Document("userData", found).append("sessionToken", token);
                    return responseDoc.toJson();
                }
            }
        }
        return null;
    }
}
