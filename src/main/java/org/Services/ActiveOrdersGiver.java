package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.MakeOrdersList;


public class ActiveOrdersGiver {
    public static ArrayList<String> GetActiveOrders(ObjectId workerId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");

        ArrayList<String> activeOrders = new ArrayList<>();
        for (Document activeOrder : activeOrdersCollection.find()) {
            if (activeOrder.get("workerId").equals(workerId)) {
                MakeOrdersList(activeOrders, clientsCollection, activeOrdersCollection, activeOrder);
            }
        }
        mongoClient.close();
        return activeOrders;
    }
}
