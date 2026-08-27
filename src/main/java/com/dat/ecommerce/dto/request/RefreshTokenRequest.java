package com.dat.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "RefreshToke is required")
    private String refreshToken;
}
