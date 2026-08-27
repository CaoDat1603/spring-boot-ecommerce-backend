package com.dat.ecommerce.entity;

import com.dat.ecommerce.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "idempotency_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_idempotency_key_user_endpoint",
                        columnNames = {
                                "idempotency_key",
                                "user_id",
                                "endpoint"
                        }
                )
        }
)
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "idempotency_key",
            nullable = false,
            length = 255
    )
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            nullable = false,
            length = 255
    )
    private String endpoint;

    @Column(
            name = "response_status",
            nullable = false
    )
    private Integer responseStatus;

    @Column(
            name = "response_body",
            columnDefinition = "TEXT"
    )
    private String responseBody;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyStatus status;

    public IdempotencyRecord(
            String idempotencyKey,
            User user,
            String endpoint
    ) {
        this.idempotencyKey = idempotencyKey;
        this.user = user;
        this.endpoint = endpoint;
        this.status = IdempotencyStatus.PROCESSING;
        this.createdAt = LocalDateTime.now();
    }
}