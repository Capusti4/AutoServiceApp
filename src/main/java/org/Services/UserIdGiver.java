package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;

public class UserIdGiver {
    public static ObjectId GetUserId(String username, String sessionToken) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = GetCollection(mongoClient, "/client/");
        Document found = usersCollection.find(new Document("username", username)).first();
        mongoClient.close();
        if (found != null) {
            for (Object token : found.get("sessionTokens", ArrayList.class)) {
                if (token.equals(sessionToken)) {
                    return (ObjectId) found.get("_id");
                }
            }
        }
        return null;
    }
}
