package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.DTO.OrderDTO;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private UserEntity worker;

    @Column()
    private BigDecimal price = null;

    @Column()
    private BigDecimal budget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private OrderTypeEntity type;

    @Column(nullable = false)
    private String status = "new";

    @Column
    private String comment;

    @Column(name = "has_customer_feedback", nullable = false)
    private boolean hasCustomerFeedback;

    @Column(name = "has_worker_feedback", nullable = false)
    private boolean hasWorkerFeedback;

    protected OrderEntity() {
    }

    public OrderEntity(OrderTypeEntity type, MakeOrderRequest request, UserEntity customer) {
        this.type = type;
        this.customer = customer;
        comment = request.comment();
        budget = request.budget();
    }

    public void start(UserEntity worker, BigDecimal price) {
        if (this.status.equals("new")) {
            this.status = "active";
            this.worker = worker;
            this.price = price;
        } else {
            throw new IncorrectOrderId();
        }
    }

    public void complete(UserEntity worker, BigDecimal price) {
        if (this.status.equals("active") & this.worker.equals(worker)) {
            this.status = "completed";
            this.price = price;
        }
    }

    public OrderDTO getDTO() {
        return new OrderDTO(
                id,
                worker == null ? null : worker.getId(),
                price,
                budget,
                type.getName(),
                status,
                comment,
                hasCustomerFeedback,
                hasWorkerFeedback
        );
    }
}
