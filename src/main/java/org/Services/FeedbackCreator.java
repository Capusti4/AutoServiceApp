package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.UnknownOrderId;
import org.bson.Document;
import org.bson.types.ObjectId;

import static org.Services.NotificationsCreator.createNotification;

public class FeedbackCreator {
    public static void createFeedback(ObjectId authorId, ObjectId targetId, ObjectId orderId, int rating, String comment) throws UnknownOrderId {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> feedbacksCollection = mongoClient.getDatabase("Users").getCollection("Feedbacks");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        Document orderDoc = completedOrdersCollection.find(new Document("_id", orderId)).first();
        if (orderDoc == null) {
            throw new UnknownOrderId();
        }
        Document feedbackDocument = new Document()
                .append("authorId", authorId)
                .append("targetId", targetId)
                .append("rating", rating)
                .append("comment", comment);
        if (authorId.equals(orderDoc.get("customerId"))) {
            completedOrdersCollection.updateOne(orderDoc, new Document("$set", new Document("hasCustomerFeedback", true)));
        } else {
            completedOrdersCollection.updateOne(orderDoc, new Document("$set", new Document("hasWorkerFeedback", true)));
        }
        feedbacksCollection.insertOne(feedbackDocument);
        String text = createText(rating, comment);
        createNotification(targetId, 4, text);
        mongoClient.close();
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
}
