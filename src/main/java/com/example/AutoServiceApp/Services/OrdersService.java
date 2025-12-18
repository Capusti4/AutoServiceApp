package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.Exceptions.IncorrectOrderId;
import com.example.AutoServiceApp.Exceptions.IncorrectSessionToken;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import com.example.AutoServiceApp.Objects.Order;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

import static com.example.AutoServiceApp.Services.ServiceFunctions.getOrders;

public class OrdersService {
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
        NotificationsService.createNotification(
                customerId,
                3,
                "Ваш заказ \"" + found.get("type") + "\" с комментарием \"" +
                found.get("comment") + "\" успешно завершен!"
        );
        NotificationsService.createNotification(
                workerId, 5, "Вы завершили заказ " + found.get("_id").toString()
        );
    }

    public static String getActiveOrders() {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        List<String> activeOrders = getOrders(activeOrdersCollection);
        if (activeOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", activeOrders) + "]";
    }

    public static String getCompletedOrders() {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        List<String> completedOrders = getOrders(completedOrdersCollection);
        if (completedOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", completedOrders) + "]";
    }

    public static String getNewOrdersList() throws IncorrectSessionToken {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        List<String> newOrders = getOrders(newOrdersCollection);
        if (newOrders.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(",", newOrders) + "]";
    }

    public static void startOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderInfo = newOrdersCollection.find(new Document("_id", orderId)).first();
        if (orderInfo == null) {
            throw new IncorrectOrderId();
        }

        newOrdersCollection.deleteOne(orderInfo);
        orderInfo.append("workerId", workerId);
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        activeOrdersCollection.insertOne(orderInfo);

        String text = "Ваш заказ \"" + orderInfo.get("type") + "\" с комментарием \"" + orderInfo.get("comment") + "\" успешно взят в работу!";
        NotificationsService.createNotification((ObjectId) orderInfo.get("customerId"), 2, text);
    }
}
