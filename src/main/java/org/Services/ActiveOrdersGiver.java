package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

import static org.Services.ServiceFunctions.getOrdersList;


public class ActiveOrdersGiver {
    public static String getActiveOrders() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        List<String> activeOrders = getOrdersList(clientsCollection, activeOrdersCollection);
        mongoClient.close();
        return "[" + String.join(",", activeOrders) + "]";
    }
}
