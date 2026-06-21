package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.FeedbackResponse;
import com.example.AutoServiceApp.DTO.SendFeedbackRequest;
import com.example.AutoServiceApp.Entity.FeedbackEntity;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Exception.IncorrectSession;
import com.example.AutoServiceApp.Repository.FeedbackRepository;
import com.example.AutoServiceApp.Repository.OrderRepository;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    public FeedbackService(
            UserRepository userRepository,
            FeedbackRepository feedbackRepository,
            NotificationService notificationService,
            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createFeedback(UserEntity author, SendFeedbackRequest request) throws IncorrectOrderId {
        OrderEntity order = orderRepository.findById(request.orderId())
                .orElseThrow(IncorrectOrderId::new);
        UserEntity target = userRepository.findById(request.targetId())
                .orElseThrow(IncorrectSession::new);
        if (author.isWorker()) {
            if (order.getCustomer().getId() == author.getId() ||
                order.getWorker().getId() == target.getId()) {
                throw new IncorrectOrderId();
            }
        } else {
            if (order.getCustomer().getId() == target.getId() ||
                order.getWorker().getId() == author.getId()) {
                throw new IncorrectOrderId();
            }
        }
        FeedbackEntity feedback = new FeedbackEntity(
                request.rating(),
                request.feedback().isEmpty() ? null : request.feedback(),
                author,
                target,
                order
        );
        feedbackRepository.save(feedback);
        String text = createText(request.rating(), feedback.getFeedback());
        notificationService.createNotification(target, text);
        if (author.isWorker()) {
            order.setWorkerFeedback();
        } else {
            order.setCustomerFeedback();
        }
    }

    private String createText(int rating, String comment) {
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

    public FeedbackResponse getFeedbacksForUser(UserEntity user) {
        return new FeedbackResponse(feedbackRepository.getByTarget(user));
    }
    public FeedbackResponse getFeedbacksForUser(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(IncorrectSession::new);
        return new FeedbackResponse(feedbackRepository.getByTarget(user));
    }

    public FeedbackResponse getFeedbacksByUser(UserEntity user) {
        return new FeedbackResponse(feedbackRepository.getByAuthor(user));
    }
}
