package main.java.com.adrian.gameconcepthub.infrastructure.web.controller;

import main.java.com.adrian.gameconcepthub.domain.model.User;
import main.java.com.adrian.gameconcepthub.domain.port.out.UserRepository;
import java.util.Objects;
import java.util.Optional;

public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public User register(String username, String password) {
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

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }
}
