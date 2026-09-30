package com.roamly.backend.service;

import com.roamly.backend.entity.User;
import com.roamly.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.roamly.backend.exception.EmailAlreadyExistsException;
import com.roamly.backend.exception.InvalidCredentialsException;

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
    throw new EmailAlreadyExistsException("Email is already in use");
}

        String passwordHash = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .passwordHash(passwordHash)
                .displayName(displayName)
                .build();

        return userRepository.save(user);
    }
    public User authenticate(String email, String password) {
        

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new InvalidCredentialsException("Invalid email or password")
            );

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        throw new InvalidCredentialsException("Invalid email or password");
    }

    return user;
}
  public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found"
                        )
                );
    }

}