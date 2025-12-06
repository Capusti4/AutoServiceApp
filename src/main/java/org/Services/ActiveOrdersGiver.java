package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static org.Services.ServiceFunctions.GetOrdersList;


public class ActiveOrdersGiver {
    public static String[] GetActiveOrders() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        mongoClient.close();
        return GetOrdersList(clientsCollection, activeOrdersCollection);
    }
}
