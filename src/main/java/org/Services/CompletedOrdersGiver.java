package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

import static org.Services.ServiceFunctions.getOrdersList;

public class CompletedOrdersGiver {
    public static String getCompletedOrders(){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        List<String> completedOrders = getOrdersList(clientsCollection, completedOrdersCollection);
        mongoClient.close();
        return "[" + String.join(",", completedOrders) + "]";
    }
}
