package com.dat.ecommerce.specification;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.OrderItem;
import com.dat.ecommerce.enums.OrderStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSpecification {
    private OrderSpecification() {
    }

    public static Specification<Order> hasStatus(
            OrderStatus status
    ) {
        return (root, query, criteraBuilder) ->
                criteraBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Order> hasUserId(
            Long userId
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("user").get("id"),
                        userId
                );
    }

    public static Specification<Order> totalAmountGreaterThanOrEqual(
            BigDecimal minTotalAmonunt
    ) {
        return (root, query, criteraBuilder) ->
                criteraBuilder.greaterThanOrEqualTo(
                        root.get("totalAmount"),
                        minTotalAmonunt
                );
    }

    public static Specification<Order> totalAmountLessThanOrEqual(
            BigDecimal maxTotalAmount
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("totalAmount"),
                        maxTotalAmount
                );
    }

    public static Specification<Order> createdAtGreaterThanOrEqual(
            LocalDateTime createdFrom
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        createdFrom
                );
    }

    public static Specification<Order> createdAtLessThanOrEqual(
            LocalDateTime createdTo
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        createdTo
                );
    }

    public static Specification<Order> hasProductSku(
            String productSku
    ) {
        return (root, query, criteriaBuilder) -> {

            Join<Order, OrderItem> orderItems =
                    root.join("orderItems");

            return criteriaBuilder.equal(
                    orderItems
                            .get("product")
                            .get("sku"),
                    productSku
            );
        };
    }
}
