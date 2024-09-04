package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.request.AuthenticationRequest;
import com.scalefocus.blog_api.request.RegisterRequest;
import com.scalefocus.blog_api.response.TokenResponse;
import com.scalefocus.blog_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(userService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(userService.login(authenticationRequest), HttpStatus.OK);
    }
}