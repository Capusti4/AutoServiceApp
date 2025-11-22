package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectOrderId;
import org.bson.Document;
import org.bson.types.ObjectId;

import static org.Services.NotificationsCreator.CreateNotification;

public class OrderCompleter {
    public static void CompleteOrder(ObjectId orderId, ObjectId workerId) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        Document found = activeOrdersCollection.find(new Document("_id", orderId).append("workerId", workerId)).first();
        if (found == null) { throw new IncorrectOrderId(); }
        activeOrdersCollection.deleteOne(found);
        completedOrdersCollection.insertOne(found
                .append("hasCustomerFeedback", false)
                .append("hasWorkerFeedback", false));
        mongoClient.close();
        SendNotifications(found);
    }

    static void SendNotifications(Document found) {
        var workerId = (ObjectId) found.get("workerId");
        var customerId = (ObjectId) found.get("customerId");
        CreateNotification(
                customerId,
                3,
                "Ваш заказ \"" + found.get("type") + "\" с комментарием \"" +
                        found.get("comment") + "\" успешно завершен!"
        );
        CreateNotification(
                workerId, 5, "Вы завершили заказ " + found.get("_id").toString()
        );
    }
}
