package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Exceptions.IncorrectOrderId;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.example.AutoServiceApp.Services.NotificationsCreator.createNotification;

public class OrderCompleter {
    public static void completeOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        Document found = activeOrdersCollection.find(new Document("_id", orderId).append("workerId", workerId)).first();
        if (found == null) {
            throw new IncorrectOrderId();
        }
        activeOrdersCollection.deleteOne(found);
        completedOrdersCollection.insertOne(found
                .append("hasCustomerFeedback", false)
                .append("hasWorkerFeedback", false));
        sendNotifications(found);
    }

    static void sendNotifications(Document found) {
        ObjectId workerId = (ObjectId) found.get("workerId");
        ObjectId customerId = (ObjectId) found.get("customerId");
        createNotification(
                customerId,
                3,
                "Ваш заказ \"" + found.get("type") + "\" с комментарием \"" +
                        found.get("comment") + "\" успешно завершен!"
        );
        createNotification(
                workerId, 5, "Вы завершили заказ " + found.get("_id").toString()
        );
    }
}
