package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByIdAndUserId(
            Long orderId,
            Long userId
    );

    List<Order> findByStatus(OrderStatus status);
}
