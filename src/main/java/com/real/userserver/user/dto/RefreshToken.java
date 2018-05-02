package com.real.userserver.user.dto;

/**
 * @author asuis
 */
public class RefreshToken {
    private String token;
    private String refreshToken;

    public String getToken() {
        return this.token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
