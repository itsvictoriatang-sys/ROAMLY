package com.roamly.backend.controller;

import com.roamly.backend.dto.RegisterRequest;
import com.roamly.backend.dto.UserResponse;
import com.roamly.backend.entity.User;
import com.roamly.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getDisplayName()
        );

        UserResponse response = UserResponse.from(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}