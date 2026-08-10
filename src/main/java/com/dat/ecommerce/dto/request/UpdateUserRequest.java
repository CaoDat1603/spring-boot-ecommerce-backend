package com.dat.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(max = 100, message = "name must not exceed 100 charcters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    UpdateUserRequest() {}
    UpdateUserRequest(
            String name,
            String email,
            String password
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
