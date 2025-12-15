package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import com.example.AutoServiceApp.Objects.Order;
import org.bson.Document;

public class OrderCreator {
    public static void createOrder(Order order) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> collection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderDoc = new Document()
                .append("customerId", order.getCustomerId())
                .append("typeID", order.getTypeID())
                .append("type", order.getType())
                .append("comment", order.getComment());
        collection.insertOne(orderDoc);
    }
}
