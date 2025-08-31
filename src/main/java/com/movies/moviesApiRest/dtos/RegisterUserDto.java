package com.movies.moviesApiRest.dtos;


public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
