package com.adrian.gameconcepthub.infrastructure.web.controller;

import com.adrian.gameconcepthub.application.service.AuthService;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.UserEntity;
import com.adrian.gameconcepthub.infrastructure.web.dto.AuthResponse;
import com.adrian.gameconcepthub.infrastructure.web.dto.LoginRequest;
import com.adrian.gameconcepthub.infrastructure.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Validar que las contraseñas coincidan
            if (!request.getPassword().equals(request.getPasswordConfirm())) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("Las contraseñas no coinciden"));
            }

            // Validar campos requeridos
            if (request.getUsername() == null || request.getUsername().isEmpty() ||
                    request.getEmail() == null || request.getEmail().isEmpty() ||
                    request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("Todos los campos son requeridos"));
            }

            UserEntity user = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthResponse.success(user.getId(), user.getUsername(), user.getEmail(), 
                            "Registro exitoso. Por favor, inicia sesión."));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            // Validar campos requeridos
            if (request.getUsername() == null || request.getUsername().isEmpty() ||
                    request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("Usuario y contraseña son requeridos"));
            }

            Optional<UserEntity> user = authService.login(request.getUsername(), request.getPassword());
            
            if (user.isPresent()) {
                // Guardar el usuario en la sesión
                session.setAttribute("userId", user.get().getId());
                session.setAttribute("username", user.get().getUsername());
                session.setAttribute("email", user.get().getEmail());
                
                return ResponseEntity.ok()
                        .body(AuthResponse.success(user.get().getId(), user.get().getUsername(), 
                                user.get().getEmail(), "Inicio de sesión exitoso"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.error("Usuario o contraseña incorrectos"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.error("Error en el servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok()
                .body(AuthResponse.success(null, null, null, "Sesión cerrada exitosamente"));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error("No hay sesión activa"));
        }

        return ResponseEntity.ok()
                .body(AuthResponse.success(userId, username, email, "Usuario autenticado"));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuth(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(userId != null);
    }
}
