package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectFeedbackType;
import com.example.AutoServiceApp.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class FeedbackController {
    private final UserService userService;
    private final FeedbackService feedbackService;

    public FeedbackController(UserService userService, FeedbackService feedbackService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/{userType}/getFeedbacks{feedbacksType}")
    public ResponseEntity<?> getFeedbacksByUser(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken,
            @PathVariable String userType,
            @PathVariable String feedbacksType
    ) {
        ServiceFunctions.checkUserType(userType);
        WithUserDataDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        FeedbackResponse response;
        if (feedbacksType.equals("ByUser")) {
            response = FeedbackService.getFeedbacksByUser(user);
        } else if (feedbacksType.equals("ForUser")) {
            response = FeedbackService.getFeedbacksForUser(user);
        } else {
            throw new IncorrectFeedbackType();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{userType}/sendFeedback")
    public ResponseEntity<?> sendFeedback(@PathVariable String userType, @RequestBody SendFeedbackRequest request) {
        ServiceFunctions.checkUserType(userType);
        feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Спасибо за Ваш отзыв!"));
    }
}
