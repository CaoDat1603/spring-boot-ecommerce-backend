package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.created_at = user.getCreatedAt();
        this.updated_at = user.getUpdatedAt();
    }
}
