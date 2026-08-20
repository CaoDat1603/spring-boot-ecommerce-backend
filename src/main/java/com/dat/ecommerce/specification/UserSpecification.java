package com.dat.ecommerce.specification;

import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    private UserSpecification() {

    }

    public static Specification<User> hasUserID(
            Long id
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("id"),
                        id
                );
    }

    public static Specification<User> hasName(
            String name
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("name"),
                        name
                );
    }

    public static Specification<User> hasEmail(
            String email
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("email"),
                        email
                );
    }

    public static Specification<User> hasRole(
            Role role
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("role"),
                        role
                );
    }

    public static Specification<User> createdAtGreaterThanOrEqual(
            LocalDateTime createdFrom
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        createdFrom
                );
    }

    public static Specification<User> createdAtLessThanOrEqual(
            LocalDateTime createdTo
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        createdTo
                );
    }
}
