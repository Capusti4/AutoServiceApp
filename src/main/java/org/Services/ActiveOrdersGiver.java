package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.MongoDBCollection;
import org.bson.Document;

import java.util.List;

import static org.Services.ServiceFunctions.getOrders;


public class ActiveOrdersGiver {
    public static String getActiveOrders() {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        List<String> activeOrders = getOrders(activeOrdersCollection);
        if (activeOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", activeOrders) + "]";
    }
}
