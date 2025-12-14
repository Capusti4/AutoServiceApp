package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectSessionToken;
import org.MongoDBCollection;
import org.bson.Document;

import java.util.List;

import static org.Services.ServiceFunctions.getOrders;

public class NewOrdersGiver {
    public static String getNewOrdersList() throws IncorrectSessionToken {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        List<String> newOrders = getOrders(newOrdersCollection);
        if (newOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", newOrders) + "]";
    }
}
