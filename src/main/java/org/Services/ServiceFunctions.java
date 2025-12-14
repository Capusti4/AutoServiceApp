package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceFunctions {
    private static final SecureRandom secureRandom = new SecureRandom(); // thread-safe
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateSessionToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64UrlEncoder.encodeToString(bytes);
    }

    public static MongoCollection<Document> getCollection(String requestURI) throws Exception {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoDatabase usersDatabase = mongoClient.getDatabase("Users");
        if (requestURI.startsWith("/client/")) {
            return usersDatabase.getCollection("Clients");
        } else if (requestURI.startsWith("/worker/")) {
            return usersDatabase.getCollection("Workers");
        } else {
            throw new Exception("Недопустимый URI");
        }
    }

    static List<String> getOrders(MongoCollection<Document> ordersCollection) {
        List<String> orders = new ArrayList<>();
        List<ObjectId> toDelete = new ArrayList<>();
        List<Document> allOrders = ordersCollection.find().into(new ArrayList<>());
        MongoClient mongoClient = MongoDBCollection.getClient();

        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        Set<Object> customersId = allOrders.stream()
                .map(order -> order.get("customerId"))
                .collect(Collectors.toSet());
        Map<Object, Document> customersMap = new HashMap<>();
        clientsCollection.find(new Document("_id", new Document("$in", customersId)))
                .forEach(customer -> customersMap.put(customer.get("_id"), customer));

        for (Document order : ordersCollection.find()) {
            Document customer = customersMap.get(order.get("customerId"));
            if (customer == null) {
                toDelete.add(new ObjectId(order.get("_id").toString()));
            } else {
                order.append("customerFirstName", customer.get("firstName"));
                order.append("customerLastName", customer.get("lastName"));
                order.append("customerPhoneNum", customer.get("phoneNum"));
                orders.add(order.toJson());
            }
        }

        if (!toDelete.isEmpty()) {
            ordersCollection.deleteMany(new Document("_id", new Document("$in", toDelete)));
        }

        return orders;
    }

    static Document getUserDocument(String username, String requestURI) throws Exception {
        MongoCollection<Document> usersCollection = getCollection(requestURI);
        return usersCollection.find(new Document("username", username)).first();
    }
}
