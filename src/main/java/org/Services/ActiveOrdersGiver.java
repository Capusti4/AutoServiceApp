package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;


public class ActiveOrdersGiver {
    public static ArrayList<String> GetActiveOrders(ObjectId workerId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");

        ArrayList<String> activeOrders = new ArrayList<>();
        for (Document activeOrder : activeOrdersCollection.find()) {
            if (activeOrder.get("workerId").equals(workerId)) {
                Document customer = clientsCollection.find(new Document("_id", activeOrder.get("customerId"))).first();
                if (customer == null) {
                    activeOrdersCollection.deleteOne(activeOrder);
                } else {
                    activeOrder.append("customerFirstName", customer.get("firstName"));
                    activeOrder.append("customerLastName", customer.get("lastName"));
                    activeOrder.append("customerPhoneNum", customer.get("phoneNum"));
                    activeOrders.add(activeOrder.toJson());
                }
            }
        }
        mongoClient.close();
        return activeOrders;
    }
}
