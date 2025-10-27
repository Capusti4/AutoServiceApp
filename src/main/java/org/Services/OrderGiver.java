package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;

public class OrderGiver {
    public static ArrayList<String> GetOrdersList(String username, String sessionToken) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> workersCollection = GetCollection(mongoClient, "/worker/");
        Document found = workersCollection.find(new Document("username", username)).first();
        if (found == null) {
            return null;
        }
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Orders").getCollection("Orders");
                ArrayList<String> ordersList = new ArrayList<>();
                for (Document doc : ordersCollection.find()) {
                    ordersList.add(doc.toJson());
                }
                mongoClient.close();
                return ordersList;
            }
        }
        return null;
    }
}
