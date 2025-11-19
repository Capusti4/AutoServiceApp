package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class NotificationsGiver {
    public static ArrayList<String> GetNotifications(ObjectId userId) {
        ArrayList<String> notifications = new ArrayList<>();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        for (Document notification : notificationsCollection.find()){
            if (notification.get("userId").equals(userId)){
                notifications.add(notification.toJson());
            }
        }
        mongoClient.close();
        return notifications;
    }
}
