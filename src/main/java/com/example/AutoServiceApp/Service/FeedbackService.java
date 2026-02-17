package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.FeedbackResponse;
import com.example.AutoServiceApp.DTO.SendFeedbackRequest;
import com.example.AutoServiceApp.Entity.FeedbackEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Exception.IncorrectSessionToken;
import com.example.AutoServiceApp.Repository.FeedbackRepository;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationService notificationService;

    public FeedbackService(
            UserRepository userRepository,
            FeedbackRepository feedbackRepository,
            NotificationService notificationService
    ) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
    }

    public void createFeedback(SendFeedbackRequest request) throws IncorrectOrderId {
        UserEntity user = userRepository.findByUsername(request.username());
        UserEntity author = userRepository.findById(request.authorId())
                .orElseThrow(IncorrectSessionToken::new);
        if (user == null || !author.checkSessionToken(request.sessionToken()) || !author.getId().equals(user.getId())) {
            throw new IncorrectSessionToken();
        }
        UserEntity target = userRepository.findById(request.targetId())
                .orElseThrow(IncorrectSessionToken::new);
        FeedbackEntity feedback = new FeedbackEntity(
                request.rating(),
                request.feedback(),
                author,
                target
        );
        feedbackRepository.save(feedback);
        String text = createText(request.rating(), request.feedback());
        notificationService.createNotification(target, 4, text);
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

    public static FeedbackResponse getFeedbacksForUser(UserEntity user) {
        return new FeedbackResponse(user.getFeedbacksForUser());
    }

    public static FeedbackResponse getFeedbacksByUser(UserEntity user) {
        return new FeedbackResponse(user.getFeedbacksByUser());
    }
}
