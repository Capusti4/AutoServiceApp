package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Exceptions.IncorrectNotificationType;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationsCreator {
    public static void createNotification(ObjectId userId, int typeId, String text) {
        if (typeId < 1 || typeId > 5) {
            throw new IncorrectNotificationType();
        }
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document notificationDocument;
        notificationDocument = new Document()
                .append("userId", userId)
                .append("typeId", typeId)
                .append("text", text)
                .append("isRead", false);
        notificationsCollection.insertOne(notificationDocument);
    }
}


