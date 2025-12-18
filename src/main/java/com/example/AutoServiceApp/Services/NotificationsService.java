package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.Exceptions.IncorrectNotificationId;
import com.example.AutoServiceApp.Exceptions.IncorrectNotificationType;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class NotificationsService {
    public static void deleteNotification(ObjectId notificationId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notifications = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document find = notifications.find(new Document("_id", notificationId)).first();
        if (find == null) {
            throw new IncorrectNotificationId();
        }
        notifications.deleteOne(find);
    }

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

    public static int getNotificationsAmount(ObjectId userId) {
        int amount = 0;
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        for (Document notification : notificationsCollection.find()){
            if (notification.get("userId").equals(userId) && !(boolean) notification.get("isRead")){
                amount++;
            }
        }
        return amount;
    }

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

    public static String getNotifications(ObjectId userId) {
        List<String> notifications = new ArrayList<>();
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        for (Document notification : notificationsCollection.find()){
            if (notification.get("userId").equals(userId)){
                notifications.add(notification.toJson());
            }
        }
        return "[" + String.join(",", notifications) + "]";
    }
}
