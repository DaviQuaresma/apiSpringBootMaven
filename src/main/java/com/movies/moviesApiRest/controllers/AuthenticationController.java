package com.movies.moviesApiRest.controllers;

import com.movies.moviesApiRest.dtos.LoginUserDto;
import com.movies.moviesApiRest.dtos.RegisterUserDto;
import com.movies.moviesApiRest.models.User;
import com.movies.moviesApiRest.services.AuthenticationService;
import com.movies.moviesApiRest.services.JwtService;
import com.movies.moviesApiRest.responses.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registerUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpire(jwtService.getExpirationTime());
        loginResponse.setEmail(authenticatedUser.getEmail());

        return ResponseEntity.ok(loginResponse);
    }

}
