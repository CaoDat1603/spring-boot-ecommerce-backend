package com.dat.ecommerce.specification;

import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    private ProductSpecification() {
    }

    public static Specification<Product> hasStatus(
            ProductStatus status
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Product> nameContains(
            String name
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("name")
                        ),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Product> skuContains(
            String sku
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("sku")
                        ),
                        "%" + sku.toLowerCase() + "%"
                );
    }

    public static Specification<Product> priceGreaterThanOrEqual(
            BigDecimal minPrice
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                );
    }

    public static Specification<Product> priceLessThanOrEqual(
            BigDecimal maxPrice
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                );
    }

    public static Specification<Product> stockGreaterThanOrEqual(
            Integer minStock
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("stock"),
                        minStock
                );
    }
}
