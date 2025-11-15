package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectOrderId;
import org.bson.Document;
import org.bson.types.ObjectId;

public class OrderStarter {
    public static void StartOrder(ObjectId orderId, ObjectId workerId) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderInfo = newOrdersCollection.find(new Document("_id", orderId)).first();
        if (orderInfo == null) { throw new IncorrectOrderId(); }

        newOrdersCollection.deleteOne(orderInfo);
        orderInfo.append("workerId", workerId);
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        activeOrdersCollection.insertOne(orderInfo);

        mongoClient.close();
    }
}
