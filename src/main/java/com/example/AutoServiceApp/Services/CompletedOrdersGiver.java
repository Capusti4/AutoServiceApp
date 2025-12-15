package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;

import java.util.List;

import static com.example.AutoServiceApp.Services.ServiceFunctions.getOrders;

public class CompletedOrdersGiver {
    public static String getCompletedOrders() {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        List<String> completedOrders = getOrders(completedOrdersCollection);
        if (completedOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", completedOrders) + "]";
    }
}
