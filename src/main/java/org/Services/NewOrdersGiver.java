package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectSessionToken;
import org.bson.Document;

import static org.Services.ServiceFunctions.getOrdersList;

public class NewOrdersGiver {
    public static String[] getNewOrdersList() throws IncorrectSessionToken {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        String[] orders = getOrdersList(clientsCollection, newOrdersCollection);
        mongoClient.close();
        return orders;
    }
}
