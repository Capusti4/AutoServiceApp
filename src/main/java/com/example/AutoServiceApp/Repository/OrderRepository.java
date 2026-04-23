package com.example.AutoServiceApp.Repository;

import com.example.AutoServiceApp.DTO.OrderDTO;
import com.example.AutoServiceApp.Entity.OrderEntity;
import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<OrderEntity, UUID> {

    @Query("""
            SELECT new com.example.AutoServiceApp.DTO.OrderDTO(
                o.id,
                w.id,
                o.price,
                o.budget,
                o.type.name,
                o.status,
                o.comment,
                o.hasCustomerFeedback,
                o.hasWorkerFeedback
            )
            FROM OrderEntity o
            LEFT JOIN o.worker w
            WHERE o.status = :status
            """)
    Collection<? extends OrderDTO> findAllByStatus(String status);

    @Query("""
            SELECT new com.example.AutoServiceApp.DTO.OrderDTO(
                o.id,
                o.worker.id,
                o.price,
                o.budget,
                o.type.name,
                o.status,
                o.comment,
                o.hasCustomerFeedback,
                o.hasWorkerFeedback
            )
            FROM OrderEntity o
            WHERE o.worker = :worker
            """)
    Collection<OrderDTO> findAllByWorker(UserEntity worker);
}
