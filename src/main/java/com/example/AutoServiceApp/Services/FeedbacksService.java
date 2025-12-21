package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.FeedbackDTO;
import com.example.AutoServiceApp.DTO.FeedbackResponse;
import com.example.AutoServiceApp.DTO.SendFeedbackRequest;
import com.example.AutoServiceApp.Exceptions.IncorrectOrderId;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FeedbacksService {
    public static void createFeedback(SendFeedbackRequest request) throws IncorrectOrderId {
        ObjectId authorId = new ObjectId(request.authorId());
        ObjectId targetId = new ObjectId(request.targetId());
        ObjectId orderId = new ObjectId(request.orderId());
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Users").getCollection("Orders");

        Document orderDoc = OrdersService.getOrder(completedOrdersCollection, orderId);

        Document feedbackDocument = new Document()
                .append("authorId", authorId)
                .append("targetId", targetId)
                .append("rating", request.rating())
                .append("comment", request.comment());
        if (authorId.equals(orderDoc.get("customerId"))) {
            completedOrdersCollection.updateOne(orderDoc, new Document("$set", new Document("hasCustomerFeedback", true)));
        } else {
            completedOrdersCollection.updateOne(orderDoc, new Document("$set", new Document("hasWorkerFeedback", true)));
        }
        feedbacksCollection.insertOne(feedbackDocument);
        String text = createText(request.rating(), request.comment());
        NotificationsService.createNotification(targetId, 4, text);
    }

    static String createText(int rating, String comment) {
        String rightWordForm;
        if (rating == 1) {
            rightWordForm = " звезда";
        } else if (rating == 5) {
            rightWordForm = " звезд";
        } else {
            rightWordForm = " звезды";
        }
        if (comment == null) {
            return "Вам оставили отзыв:\n" + rating + rightWordForm;
        }
        return "Вам оставили отзыв:\n" + rating + rightWordForm + "\n\"" + comment + "\"";
    }

    public static FeedbackResponse getFeedbacksForUser(ObjectId userId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        List<FeedbackDTO> feedbacks = new ArrayList<>();
        for (Document feedback : feedbacksCollection.find()) {
            if (feedback.get("targetId").equals(userId)) {
                FeedbackDTO feedbackDTO = new FeedbackDTO(
                        (ObjectId) feedback.get("_id"),
                        (ObjectId) feedback.get("authorId"),
                        (ObjectId) feedback.get("targetId"),
                        (Integer) feedback.get("rating"),
                        (String) feedback.get("comment")
                );
                feedbacks.add(feedbackDTO);
            }
        }
        return new FeedbackResponse(feedbacks);
    }

    public static FeedbackResponse getFeedbacksByUser(ObjectId userId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        List<FeedbackDTO> feedbacks = new ArrayList<>();
        for (Document feedback : feedbacksCollection.find()) {
            if (feedback.get("authorId").equals(userId)) {
                FeedbackDTO feedbackDTO = new FeedbackDTO(
                        (ObjectId) feedback.get("_id"),
                        (ObjectId) feedback.get("authorId"),
                        (ObjectId) feedback.get("targetId"),
                        (Integer) feedback.get("rating"),
                        (String) feedback.get("comment")
                );
                feedbacks.add(feedbackDTO);
            }
        }
        return new FeedbackResponse(feedbacks);
    }
}
