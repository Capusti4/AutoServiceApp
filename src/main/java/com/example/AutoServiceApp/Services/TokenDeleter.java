package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.example.AutoServiceApp.Services.ServiceFunctions.getCollection;

public class TokenDeleter {
    public static void deleteSessionToken(String username, String token, String userType) {
        MongoCollection<Document> usersCollection = getCollection(userType);

        Bson filter = Filters.eq("username", username);
        Bson update = Updates.pull("sessionTokens", token);

        usersCollection.updateOne(filter, update);
    }
}
