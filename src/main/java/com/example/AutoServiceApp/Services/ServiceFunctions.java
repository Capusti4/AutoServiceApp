package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.Exceptions.IncorrectUserType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.*;

public class ServiceFunctions {
    private static final SecureRandom secureRandom = new SecureRandom(); // thread-safe
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateSessionToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64UrlEncoder.encodeToString(bytes);
    }

    public static MongoCollection<Document> getCollection(String userType) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoDatabase usersDatabase = mongoClient.getDatabase("Users");
        if (userType.equals("client")) {
            return usersDatabase.getCollection("Clients");
        } else if (userType.equals("worker")) {
            return usersDatabase.getCollection("Workers");
        } else {
            throw new IncorrectUserType();
        }
    }

    static Document getUserDocument(String username, String userType) {
        MongoCollection<Document> usersCollection = getCollection(userType);
        return usersCollection.find(new Document("username", username)).first();
    }
}
