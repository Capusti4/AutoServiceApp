package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;

public class OrderGiver {
    public static ArrayList<String> GetOrdersList(String username, String sessionToken) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> workersCollection = GetCollection(mongoClient, "/worker/");
        Document found = workersCollection.find(new Document("username", username)).first();
        if (found == null) {
            return null;
        }
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Orders").getCollection("Orders");
                ArrayList<String> ordersList = new ArrayList<>();
                for (Document doc : ordersCollection.find()) {
                    MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
                    Document customer = clientsCollection.find(new Document("_id", doc.get("customerId"))).first();
                    if (customer == null) {
                        doc.append("customerId", null);
                    } else {
                        doc.append("customerFirstName", customer.get("firstName"));
                        doc.append("customerLastName", customer.get("lastName"));
                        doc.append("customerPhoneNum", customer.get("phoneNum"));
                        ordersList.add(doc.toJson());
                    }
                }
                mongoClient.close();
                return ordersList;
            }
        }
        return null;
    }
}
