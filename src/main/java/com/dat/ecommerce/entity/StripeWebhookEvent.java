package com.dat.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "stripe_webhook_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stripe_webhook_event_id",
                        columnNames = "event_id"
                )
        }
)
public class StripeWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false)
    private boolean processed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;



    public StripeWebhookEvent(
            String eventId,
            String eventType
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }
}
