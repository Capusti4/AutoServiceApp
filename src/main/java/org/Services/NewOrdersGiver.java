package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static org.Services.ServiceFunctions.GetCollection;

public class NewOrdersGiver {
    public static ArrayList<String> GetNewOrdersList(String username) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> workersCollection = GetCollection(mongoClient, "/worker/");
        Document found = workersCollection.find(new Document("username", username)).first();
        if (found == null) {
            return null;
        }
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        ArrayList<String> ordersList = new ArrayList<>();
        for (Document doc : ordersCollection.find()) {
            Document customer = clientsCollection.find(new Document("_id", doc.get("customerId"))).first();
            if (customer == null) {
                ordersCollection.deleteOne(doc);
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
