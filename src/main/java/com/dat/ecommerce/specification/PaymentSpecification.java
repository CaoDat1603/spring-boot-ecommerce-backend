package com.dat.ecommerce.specification;

import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentSpecification {
    private PaymentSpecification() {}

    public static Specification<Payment> hasStatus(
            PaymentStatus status
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("paymentStatus"),
                        status
                );
    }

    public static Specification<Payment> hasMethod(
            PaymentMethod method
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("method"),
                        method
                );
    }

    public static Specification<Payment> hasUserId(
            Long userId
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("order").get("user").get("id"),
                        userId
                );
    }

    public static Specification<Payment> AmountGreaterThanOrEqual(
            BigDecimal minAmount
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("amount"),
                        minAmount
                );
    }

    public static Specification<Payment> AmountLessThanOrEqual(
            BigDecimal maxAmount
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("amount"),
                        maxAmount
                );
    }

    public static Specification<Payment> createdAtGreaterThanOrEqual(
            LocalDateTime createdFrom
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        createdFrom
                );
    }

    public static Specification<Payment> createdAtLessThanOrEqual(
            LocalDateTime createdTo
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        createdTo
                );
    }

    public static Specification<Payment> paidAtGreaterThanOrEqual(
            LocalDateTime paidFrom
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("paidAt"),
                        paidFrom
                );
    }

    public static Specification<Payment> paidAtLessThanOrEqual(
            LocalDateTime paidTo
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("paidAt"),
                        paidTo
                );
    }
}
