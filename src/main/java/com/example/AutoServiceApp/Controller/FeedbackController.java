package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectFeedbackType;
import com.example.AutoServiceApp.Service.*;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/getFeedbacks{feedbacksType}")
    public ResponseEntity<?> getFeedbacks(
            @PathVariable String feedbacksType,
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        FeedbackResponse response;
        if (feedbacksType.equals("ByUser")) {
            response = feedbackService.getFeedbacksByUser(user);
        } else if (feedbacksType.equals("ForUser")) {
            response = feedbackService.getFeedbacksForUser(user);
        } else {
            throw new IncorrectFeedbackType();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getUserFeedbacks/{userId}")
    public ResponseEntity<?> getUserFeedbacks(
            @PathVariable long userId
    ) {
        FeedbackResponse response = feedbackService.getFeedbacksForUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/sendFeedback")
    public ResponseEntity<?> sendFeedback(
            @RequestBody SendFeedbackRequest request,
            HttpSession session
    ) {
        UserEntity user = userService.getUser(session);
        feedbackService.createFeedback(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Спасибо за Ваш отзыв!"));
    }
}
