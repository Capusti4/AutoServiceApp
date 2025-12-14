package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ServiceFunctions {
    private static final SecureRandom secureRandom = new SecureRandom(); // thread-safe
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateSessionToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64UrlEncoder.encodeToString(bytes);
    }

    public static MongoCollection<Document> getCollection(MongoClient mongoClient, String requestURI) throws Exception {
        MongoDatabase usersDatabase = mongoClient.getDatabase("Users");
        if (requestURI.startsWith("/client/")) {
            return usersDatabase.getCollection("Clients");
        } else if (requestURI.startsWith("/worker/")) {
            return usersDatabase.getCollection("Workers");
        } else {
            throw new Exception("Недопустимый URI");
        }
    }

    static List<String> getOrdersList(MongoCollection<Document> clientsCollection,
                                  MongoCollection<Document> ordersCollection) {
        List<String> orders = new ArrayList<>();
        for (Document order : ordersCollection.find()) {
            Document customer = clientsCollection.find(new Document("_id", order.get("customerId"))).first();
            if (customer == null) {
                ordersCollection.deleteOne(order);
            } else {
                order.append("customerFirstName", customer.get("firstName"));
                order.append("customerLastName", customer.get("lastName"));
                order.append("customerPhoneNum", customer.get("phoneNum"));
                orders.add(order.toJson());
            }
        }
        return orders;
    }

    static Document getUserDocument(String username, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = getCollection(mongoClient, requestURI);
        Document found = usersCollection.find(new Document("username", username)).first();
        mongoClient.close();
        return found;
    }
}
