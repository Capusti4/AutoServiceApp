package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;
import static org.Services.ServiceFunctions.MakeOrdersList;

public class NewOrdersGiver {
    public static String[] GetNewOrdersList(String username) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> workersCollection = GetCollection(mongoClient, "/worker/");
        Document found = workersCollection.find(new Document("username", username)).first();
        if (found == null) { return null; }
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        ArrayList<String> newOrders = new ArrayList<>();
        for (Document newOrder : ordersCollection.find()) {
            MakeOrdersList(newOrders, clientsCollection, ordersCollection, newOrder);
        }
        mongoClient.close();
        return newOrders.toArray(new String[0]);
    }
}
