package com.example.AutoServiceApp.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationsAmountGiver {
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
}
