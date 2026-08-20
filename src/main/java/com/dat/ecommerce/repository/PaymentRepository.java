package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.PaymentProvider;
import com.dat.ecommerce.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>,
        JpaSpecificationExecutor<Payment> {

    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByOrderIdAndStatus( Long orderId, PaymentStatus status );
    Optional<Payment> findByProviderAndProviderPaymentId(
            PaymentProvider provider,
            String providerPaymentId
    );
}
