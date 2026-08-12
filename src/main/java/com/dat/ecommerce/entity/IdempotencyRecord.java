package com.dat.ecommerce.entity;

import jakarta.persistence.*;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;

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

    public IdempotencyRecord() {
    }

    public IdempotencyRecord(
            String idempotencyKey,
            User user,
            String endpoint
    ) {
        this.idempotencyKey = idempotencyKey;
        this.user = user;
        this.endpoint = endpoint;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public User getUser() {
        return user;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}