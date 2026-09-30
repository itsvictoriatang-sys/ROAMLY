package com.roamly.backend.controller;

import com.roamly.backend.dto.LoginRequest;
import com.roamly.backend.dto.UserResponse;
import com.roamly.backend.entity.User;
import com.roamly.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.roamly.backend.dto.LoginResponse;
import com.roamly.backend.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
private final JwtService jwtService;

    public AuthController(UserService userService,
                      JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
}

    @PostMapping("/login")
public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request) {

    User user = userService.authenticate(
            request.getEmail(),
            request.getPassword()
    );

    String token = jwtService.generateToken(user.getEmail());

    LoginResponse response = LoginResponse.builder()
            .token(token)
            .user(UserResponse.from(user))
            .build();

    return ResponseEntity.ok(response);
}
}