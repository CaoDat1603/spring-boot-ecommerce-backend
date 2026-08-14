package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.StripeWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StripeWebhookEventRepository extends JpaRepository<StripeWebhookEvent, Long> {
    boolean existsByEventId(String eventId);

    Optional<StripeWebhookEvent> findByEventId(
            String eventId
    );
}
