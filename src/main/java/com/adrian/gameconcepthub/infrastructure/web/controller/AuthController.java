package com.adrian.gameconcepthub.infrastructure.web.controller;

import com.adrian.gameconcepthub.domain.model.User;
import com.adrian.gameconcepthub.domain.port.out.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @PostMapping("/register")
    public User register(@RequestParam String username, @RequestParam String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(new User(null, username.trim(), password, "ADMIN"));
    }

    @PostMapping("/login")
    public Optional<User> login(@RequestParam String username, @RequestParam String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }
}
