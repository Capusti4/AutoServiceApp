package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectSessionToken;
import org.bson.Document;

import static org.Services.ServiceFunctions.GetOrdersList;

public class NewOrdersGiver {
    public static String[] GetNewOrdersList() throws IncorrectSessionToken {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        mongoClient.close();
        return GetOrdersList(clientsCollection, ordersCollection);
    }
}
