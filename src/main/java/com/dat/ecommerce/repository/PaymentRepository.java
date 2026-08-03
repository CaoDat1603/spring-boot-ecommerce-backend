package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
    boolean existsByOrder(Order order);
    long countByOrder_Id(Long orderId);
}
