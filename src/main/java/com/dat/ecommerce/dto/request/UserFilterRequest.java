package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.Role;

import java.time.LocalDateTime;

public class UserFilterRequest {
    private Long id;

    private String name;

    private String email;

    private Role role;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

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

    public LocalDateTime getCreatedFrom() {
        return createdFrom;
    }

    public LocalDateTime getCreatedTo() {
        return createdTo;
    }

    public void setCreatedFrom(LocalDateTime createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setCreatedTo(LocalDateTime createdTo) {
        this.createdTo = createdTo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}


