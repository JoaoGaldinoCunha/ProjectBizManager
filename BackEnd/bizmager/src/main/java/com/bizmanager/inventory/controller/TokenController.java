package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.request.LoginRequest;
import com.bizmanager.inventory.model.response.LoginResponse;
import com.bizmanager.inventory.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return tokenService.authenticationLogin(loginRequest);
    }
}
