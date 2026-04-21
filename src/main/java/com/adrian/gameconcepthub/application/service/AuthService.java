package com.adrian.gameconcepthub.application.service;

import com.adrian.gameconcepthub.infrastructure.persistence.entity.UserEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<UserEntity> login(String username, String password) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public UserEntity register(String username, String email, String password) throws Exception {
        // Validar que el usuario no exista
        if (userRepository.existsByUsername(username)) {
            throw new Exception("El nombre de usuario ya está registrado");
        }

        // Validar que el email no exista
        if (userRepository.existsByEmail(email)) {
            throw new Exception("El correo electrónico ya está registrado");
        }

        // Crear nuevo usuario
        UserEntity user = new UserEntity(username, passwordEncoder.encode(password), email);
        user.setRole("USER");

        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
