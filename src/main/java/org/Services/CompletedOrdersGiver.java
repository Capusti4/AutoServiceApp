package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static org.Services.ServiceFunctions.getOrdersList;

public class CompletedOrdersGiver {
    public static String[] getCompletedOrders(){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        String[] orders = getOrdersList(clientsCollection, completedOrdersCollection);
        mongoClient.close();
        return orders;
    }
}
