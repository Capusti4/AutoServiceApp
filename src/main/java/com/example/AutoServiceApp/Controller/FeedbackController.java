package com.example.AutoServiceApp.Controller;

import com.example.AutoServiceApp.DTO.*;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectUserType;
import com.example.AutoServiceApp.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class FeedbackController {
    private final UserService userService;
    private final FeedbacksService feedbacksService;

    public FeedbackController(UserService userService, FeedbacksService feedbacksService) {
        this.userService = userService;
        this.feedbacksService = feedbacksService;
    }

    @GetMapping("/{userType}/getFeedbacksByUser")
    public ResponseEntity<?> getFeedbacksByUser(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken,
            @PathVariable String userType) {
        if (!userType.equals("client") && !userType.equals("worker")) {
            throw new IncorrectUserType();
        }
        WithUserDataDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        FeedbackResponse response = FeedbacksService.getFeedbacksByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userType}/getFeedbacksForUser")
    public ResponseEntity<?> getFeedbacksForUser(
            @RequestHeader("Username") String username,
            @RequestHeader("Session-Token") String sessionToken,
            @PathVariable String userType) {
        if (!userType.equals("client") && !userType.equals("worker")) {
            throw new IncorrectUserType();
        }
        WithUserDataDTO session = new SessionDTO(username, sessionToken);
        UserEntity user = userService.getUser(session);
        FeedbackResponse response = FeedbacksService.getFeedbacksForUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{userType}/sendFeedback")
    public ResponseEntity<?> sendFeedback(
            @PathVariable String userType,
            @RequestBody SendFeedbackRequest request
    ) {
        ServiceFunctions.checkUserType(userType);
        feedbacksService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Спасибо за Ваш отзыв!"));
    }
}
