package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.LoginRequest;
import com.dat.ecommerce.dto.request.RegisterRequest;
import com.dat.ecommerce.dto.response.AuthResponse;
import com.dat.ecommerce.dto.response.UserResponse;
import com.dat.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // @Valid kiểm tra các annotation validation trong object này trước khi chạy method
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }
}
