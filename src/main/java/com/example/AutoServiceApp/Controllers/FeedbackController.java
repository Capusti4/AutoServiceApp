package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.Services.FeedbackCreator;
import com.example.AutoServiceApp.Services.FeedbacksGiver;
import com.example.AutoServiceApp.Services.TokenChecker;
import com.example.AutoServiceApp.Services.UserIdGiver;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class FeedbackController {

    @PostMapping("/{userType}/getFeedbacksByUser")
    public ResponseEntity<?> getFeedbacksByUser(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        ObjectId userId = UserIdGiver.getUserId(data, userType);
        String feedbacksJson = FeedbacksGiver.getFeedbacksByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(feedbacksJson);
    }

    @PostMapping("/{userType}/getFeedbacksForUser")
    public ResponseEntity<?> getFeedbacksForUser(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        ObjectId userId = UserIdGiver.getUserId(data, userType);
        String feedbacksJson = FeedbacksGiver.getFeedbacksForUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(feedbacksJson);
    }

    @PostMapping("/{userType}/sendFeedback")
    public ResponseEntity<?> sendFeedback(
            @PathVariable String userType,
            @RequestBody Map<String, Object> data
    ) {
        TokenChecker.checkUserToken(data, userType);
        ObjectId authorId = new ObjectId((String) data.get("authorId"));
        ObjectId targetId = new ObjectId((String) data.get("targetId"));
        ObjectId orderId = new ObjectId((String) data.get("orderId"));
        int rating = Math.toIntExact(Math.round((double) data.get("rating")));
        String comment = (String) data.get("comment");
        FeedbackCreator.createFeedback(authorId, targetId, orderId, rating, comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Спасибо за Ваш отзыв!"));
    }
}
