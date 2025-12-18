package com.example.AutoServiceApp.Controllers;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Services.*;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class FeedbackController {
    @GetMapping("/{userType}/getFeedbacksByUser")
    public ResponseEntity<?> getFeedbacksByUser(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        WithUserDataDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        FeedbackResponse response = FeedbacksService.getFeedbacksByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userType}/getFeedbacksForUser")
    public ResponseEntity<?> getFeedbacksForUser(
            @PathVariable String userType,
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken
    ) {
        WithUserDataDTO user = new SessionDTO(username, sessionToken);
        ObjectId userId = UserIdService.getUserId(user, userType);
        FeedbackResponse response = FeedbacksService.getFeedbacksForUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{userType}/sendFeedback")
    public ResponseEntity<?> sendFeedback(
            @PathVariable String userType,
            @RequestBody SendFeedbackRequest request
    ) {
        TokensService.checkUserToken(request, userType);
        FeedbacksService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Спасибо за Ваш отзыв!"));
    }
}
