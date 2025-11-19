package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectNotificationId;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationReader {
    public static void ReadNotification(ObjectId notificationId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document found = notificationsCollection.find(new Document("_id", notificationId)).first();
        if (found != null) {
            notificationsCollection.updateOne(found, new Document("$set", new Document("isRead", true)));
        } else {
            throw new IncorrectNotificationId();
        }
        mongoClient.close();
    }
}
