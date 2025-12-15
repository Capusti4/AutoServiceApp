package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Exceptions.IncorrectNotificationId;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationReader {
    public static void readNotification(ObjectId notificationId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document found = notificationsCollection.find(new Document("_id", notificationId)).first();
        if (found != null) {
            notificationsCollection.updateOne(found, new Document("$set", new Document("isRead", true)));
        } else {
            throw new IncorrectNotificationId();
        }
    }

    public static void readAllNotifications(ObjectId userId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        for (Document notification : notificationsCollection.find(new Document("userId", userId).append("isRead", false))) {
            notificationsCollection.updateOne(notification, new Document("$set", new Document("isRead", true)));
        }
    }
}
