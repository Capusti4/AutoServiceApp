package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectOrderId;
import org.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static org.Services.NotificationsCreator.createNotification;

public class OrderStarter {
    public static void startOrder(ObjectId orderId, ObjectId workerId) throws Exception {
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
        createNotification((ObjectId) orderInfo.get("customerId"), 2, text);
    }
}
