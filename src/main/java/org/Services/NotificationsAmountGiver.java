package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationsAmountGiver {
    public static int getNotificationsAmount(ObjectId userId) {
        int amount = 0;
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        mongoClient.close();
        for (Document notification : notificationsCollection.find()){
            if (notification.get("userId").equals(userId) && !(boolean) notification.get("isRead")){
                amount++;
            }
        }
        return amount;
    }
}
