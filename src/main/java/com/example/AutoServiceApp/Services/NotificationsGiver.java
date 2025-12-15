package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class NotificationsGiver {
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
