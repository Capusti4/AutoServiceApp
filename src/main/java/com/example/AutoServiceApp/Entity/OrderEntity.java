package com.example.AutoServiceApp.Entity;

import com.example.AutoServiceApp.DTO.MakeOrderRequest;
import com.example.AutoServiceApp.Exception.IncorrectOrderId;
import com.example.AutoServiceApp.Exception.IncorrectOrderTypeId;
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

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "type_id", nullable = false)
    private int typeId;

    @Column(nullable = false)
    private String status;

    @Column
    private String comment;

    @Column(name = "has_customer_feedback", nullable = false)
    private boolean hasCustomerFeedback;

    @Column(name = "has_worker_feedback", nullable = false)
    private boolean hasWorkerFeedback;

    protected OrderEntity() {
    }

    public OrderEntity(MakeOrderRequest request, UserEntity customer) {
        typeId = request.orderTypeId();
        if (typeId == 0) {
            type = request.orderType();
        } else {
            SwitchTypeId();
        }
        this.customer = customer;
        this.comment = request.comment();
        status = "new";
    }

    void SwitchTypeId() {
        switch (this.typeId) {
            case 1:
                this.type = "Покраска авто";
                break;
            case 2:
                this.type = "Замена шин";
                break;
            case 3:
                this.type = "Замена внешней детали";
                break;
            case 4:
                this.type = "Замена внутренней детали";
                break;
            case 5:
                this.type = "Ремонт внешней детали";
                break;
            case 6:
                this.type = "Ремонт внутренней детали";
                break;
            default:
                throw new IncorrectOrderTypeId();
        }
    }

    public void start(UserEntity worker) {
        if (this.status.equals("new")) {
            this.status = "active";
            this.worker = worker;
        } else {
            throw new IncorrectOrderId();
        }
    }

    public void complete(UserEntity worker) {
        if (this.status.equals("active") & this.worker.equals(worker)) {
            this.status = "completed";
        }
    }

}
