package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static org.Services.NotificationsCreator.CreateNotification;

public class FeedbackCreator {
    public static void CreateFeedback(ObjectId authorId, ObjectId targetId, int rating, String comment) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");

        Document feedbackDocument = new Document()
                .append("authorId", authorId)
                .append("targetId", targetId)
                .append("rating", rating)
                .append("comment", comment);
        feedbacksCollection.insertOne(feedbackDocument);
        String text = CreateText(rating, comment);
        CreateNotification(targetId, null, 4, text);
        mongoClient.close();
    }

    static String CreateText(int rating, String comment) {
        String rightWordForm;
        if (rating == 1) {
            rightWordForm = " звезда";
        } else if (rating == 5) {
            rightWordForm =  " звезд";
        } else {
            rightWordForm = " звезды";
        }
        if (comment == null) { return "Вам оставили отзыв:\n" + rating + rightWordForm; }
        return "Вам оставили отзыв:\n" + rating + rightWordForm + "\n\"" + comment + "\"";
    }
}
