package com.dat.ecommerce.specification;

import com.dat.ecommerce.entity.CartItem;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemSpecification {
    private CartItemSpecification() {

    }
    public static Specification<CartItem> hasCartId(
            Long cartId
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("cart").get("id"),
                        cartId
                );
    }

    public static Specification<CartItem> hasProductId(
            Long productId
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("product").get("id"),
                        productId
                );
    }

    public static Specification<CartItem> hasProductName(
            String name
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("product").get("name"),
                        name
                );
    }

    public static Specification<CartItem> hasProductSku(
            String sku
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("product").get("sku"),
                        sku
                );
    }

    public static Specification<CartItem> quantityGreaterThanOrEqual(
            Integer quantityMin
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("quantity"),
                        quantityMin
                );
    }

    public static Specification<CartItem> quantityLessThanOrEqual(
            Integer quantityMax
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("quantity"),
                        quantityMax
                );
    }

    public static Specification<CartItem> priceGreaterThanOrEqual(
            BigDecimal priceMin
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        priceMin
                );
    }

    public static Specification<CartItem> priceLessThanOrEqual(
            BigDecimal priceMax
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        priceMax
                );
    }

    public static Specification<CartItem> createdAtGreaterThanOrEqual(
            LocalDateTime createdFrom
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        createdFrom
                );
    }

    public static Specification<CartItem> createdAtLessThanOrEqual(
            LocalDateTime createdTo
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        createdTo
                );
    }
}
