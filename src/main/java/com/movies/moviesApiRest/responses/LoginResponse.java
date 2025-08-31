package com.movies.moviesApiRest.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpire() {
        return expiresIn;
    }

    public void setExpire(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
