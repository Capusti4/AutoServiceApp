package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Exceptions.IncorrectNotificationId;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationDeleter {
    public static void deleteNotification(ObjectId notificationId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> notifications = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document find = notifications.find(new Document("_id", notificationId)).first();
        if (find == null) {
            throw new IncorrectNotificationId();
        }
        notifications.deleteOne(find);
    }
}
