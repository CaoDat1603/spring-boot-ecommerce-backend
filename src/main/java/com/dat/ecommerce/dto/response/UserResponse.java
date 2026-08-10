package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public UserResponse() {}
    public UserResponse(
            Long id,
            String name,
            String email,
            Role role,
            LocalDateTime created_at,
            LocalDateTime updated_at
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.created_at = user.getCreatedAt();
        this.updated_at = user.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}
