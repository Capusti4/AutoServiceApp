package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.MakeOrdersList;

public class CompletedOrdersGiver {
    public static String[] GetCompletedOrders(ObjectId userId){
        ArrayList<String> completedOrders = new ArrayList<>();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        for (Document completedOrder : completedOrdersCollection.find()) {
            if (completedOrder.get("customerId").equals(userId) || completedOrder.get("workerId").equals(userId)) {
                MakeOrdersList(completedOrders, clientsCollection, completedOrdersCollection, completedOrder);
            }
        }
        mongoClient.close();
        return completedOrders.toArray(new String[0]);
    }
}
