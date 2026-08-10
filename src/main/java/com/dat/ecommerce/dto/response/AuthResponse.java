package com.dat.ecommerce.dto.response;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public AuthResponse() {
    }

    public AuthResponse(
            String accessToken,
            String refreshToken,
            String tokenType
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}