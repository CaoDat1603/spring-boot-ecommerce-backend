package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserFilterRequest {
    private Long id;

    private String name;

    private String email;

    private Role role;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;
}


