package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.LoginRequest;
import com.dat.ecommerce.dto.request.RegisterRequest;
import com.dat.ecommerce.dto.response.AuthResponse;
import com.dat.ecommerce.dto.response.UserResponse;
import com.dat.ecommerce.entity.RefreshToken;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.exception.EmailAlreadyExistsException;
import com.dat.ecommerce.exception.InvalidCredentialsException;
import com.dat.ecommerce.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// chỉ là một Annotation thông báo có Bean
// Bean là Oject được Spring quản lý trong IoC Container (kho)
// Khi gọi thì không cần phải new AuthService (bằng Dependency Injection)
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // Transactional đảm bảo:
    //Thành công → commit tất cả.
    //Có lỗi → rollback tất cả.
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists");
        }

        // BCrypt Hash
        String encoded = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encoded,
                Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String accessToken =
                jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    @Transactional
    public AuthResponse refreshAccessToken(
            String refreshTokenValue
    ) {

        RefreshToken oldRefreshToken =
                refreshTokenService.findByToken(
                        refreshTokenValue
                );

        refreshTokenService.verifyExpiration(
                oldRefreshToken
        );

        User user = oldRefreshToken.getUser();

        refreshTokenService.revokeToken(
                oldRefreshToken
        );

        String newAccessToken = jwtService.generateAccessToken(user);

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(
                        user
                );

        return new AuthResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                "Bearer"
        );
    }

    @Transactional
    public void logout(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        refreshTokenValue
                );

        refreshTokenService.revokeToken(
                refreshToken
        );
    }
}
