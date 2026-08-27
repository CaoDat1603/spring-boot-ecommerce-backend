package com.dat.ecommerce.entity;

import com.dat.ecommerce.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Hibernate(JPA) quản lý
// "@Entity, @Table, @Id, @Column, các annotation quan hệ (@OneToMany, @ManyToOne...)".
// -> quản lý việc ánh xạ giữa Object và Database.
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    // Không tự sinh ra id, để database sinh.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    // Chỉ cho Hibernate biết đang trỏ đến cột password_hash của Entity
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public User(
            String name,
            String email,
            String passwordHash,
            Role role
    ) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

