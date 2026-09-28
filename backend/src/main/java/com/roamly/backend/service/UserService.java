package com.roamly.backend.service;

import com.roamly.backend.entity.User;
import com.roamly.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String email, String password, String displayName) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .passwordHash(passwordHash)
                .displayName(displayName)
                .build();

        return userRepository.save(user);
    }
}