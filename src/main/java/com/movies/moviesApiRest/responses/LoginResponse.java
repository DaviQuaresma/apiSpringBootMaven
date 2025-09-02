package com.movies.moviesApiRest.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long expiresIn;
    private String Email;

    public void setExpire(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
