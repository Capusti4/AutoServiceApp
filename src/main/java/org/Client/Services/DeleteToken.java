package org.Client.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

public class DeleteToken {
    public static void DeleteSessionToken(String username, String token) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase usersDatabase = mongoClient.getDatabase("Clients");
        MongoCollection<Document> usersCollection = usersDatabase.getCollection("Clients");

        Bson filter = Filters.eq("username", username);
        Bson update = Updates.pull("sessionTokens", token);

        usersCollection.updateOne(filter, update);
        mongoClient.close();
    }
}
