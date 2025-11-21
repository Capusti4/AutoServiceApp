package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectNotificationType;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationsCreator {
    public static void CreateNotification(ObjectId userId, ObjectId workerId, int typeId, String text) {
        if (typeId < 1 || typeId > 4) {
            throw new IncorrectNotificationType();
        }
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document notificationDocument;
        if (workerId != null) {
            notificationDocument = new Document()
                    .append("workerId", workerId)
                    .append("userId", userId)
                    .append("typeId", typeId)
                    .append("text", text)
                    .append("isRead", false);
        } else {
            notificationDocument = new Document()
                    .append("userId", userId)
                    .append("typeId", typeId)
                    .append("text", text)
                    .append("isRead", false);
        }

        notificationsCollection.insertOne(notificationDocument);
        mongoClient.close();
    }
}


