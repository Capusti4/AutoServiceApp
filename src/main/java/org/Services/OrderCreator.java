package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Order;
import org.bson.Document;

public class OrderCreator {
    public static void createOrder(Order order) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> collection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderDoc = new Document()
                .append("customerId", order.getCustomerId())
                .append("typeID", order.getTypeID())
                .append("type", order.getType())
                .append("comment", order.getComment());
        collection.insertOne(orderDoc);
        mongoClient.close();
    }
}
