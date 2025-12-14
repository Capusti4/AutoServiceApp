package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FeedbacksGiver {
    public static String getFeedbacksForUser(ObjectId userId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        List<String> feedbacks = new ArrayList<>();
        for (Document feedback : feedbacksCollection.find()) {
            if (feedback.get("targetId").equals(userId)) {
                feedbacks.add(feedback.toJson());
            }
        }
        mongoClient.close();
        return "[" + String.join(",", feedbacks) + "]";
    }

    public static String getFeedbacksByUser(ObjectId userId) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        List<String> feedbacks = new ArrayList<>();
        for (Document feedback : feedbacksCollection.find()) {
            if (feedback.get("authorId").equals(userId)) {
                feedbacks.add(feedback.toJson());
            }
        }
        mongoClient.close();
        return "[" + String.join(",", feedbacks) + "]";
    }
}
