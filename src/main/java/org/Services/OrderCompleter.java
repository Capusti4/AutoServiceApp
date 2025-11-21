package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectOrderId;
import org.bson.Document;
import org.bson.types.ObjectId;

import static org.Services.NotificationsCreator.CreateNotification;

public class OrderCompleter {
    public static void CompleteOrder(ObjectId orderId) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");

        Document found = activeOrdersCollection.find(new Document("_id", orderId)).first();
        if (found == null) { throw new IncorrectOrderId(); }
        activeOrdersCollection.deleteOne(found);
        completedOrdersCollection.insertOne(found);
        CreateNotification(
                (ObjectId) found.get("customerId"),
                (ObjectId) found.get("workerId"),
                3,
                "Ваш заказ \"" + found.get("type") + "\" с комментарием \"" + found.get("comment") +"\" успешно завершен!"
        );
        mongoClient.close();
    }
}
