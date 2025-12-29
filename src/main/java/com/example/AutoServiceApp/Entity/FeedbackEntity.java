package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.Exception.IncorrectFeedbackRating;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private UserEntity target;

    protected FeedbackEntity() {

    }

    public FeedbackEntity(int rating, String feedback, UserEntity author, UserEntity target) {
        if (rating > 5 || rating < 1) {
            throw new IncorrectFeedbackRating();
        }
        this.rating = rating;
        this.feedback = feedback;
        this.author = author;
        this.target = target;
    }
}
