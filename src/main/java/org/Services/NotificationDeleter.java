package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectNotificationId;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificationDeleter {
    public static void deleteNotification(ObjectId notificationId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> notificationsCollection = mongoClient.getDatabase("Users").getCollection("Notifications");
        Document find = notificationsCollection.find(new Document("_id", notificationId)).first();
        if (find == null) {
            throw new IncorrectNotificationId();
        }
        notificationsCollection.deleteOne(find);
        mongoClient.close();
    }
}
