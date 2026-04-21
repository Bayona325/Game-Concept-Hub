package com.adrian.gameconcepthub.infrastructure.web.controller;

import com.adrian.gameconcepthub.infrastructure.persistence.entity.GameEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.UserEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.CategoryEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.TagEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaGameRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaUserRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaCategoryRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GameController {

    @Autowired
    private JpaGameRepository gameRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaTagRepository tagRepository;

    private UserEntity getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameEntity gameData, HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        try {
            GameEntity game = new GameEntity(gameData.getName(), gameData.getDescription(), 
                                            gameData.getGenre(), user);
            gameRepository.save(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGames(HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        List<GameEntity> games = gameRepository.findByUser(user);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchGames(@RequestParam String query, HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        List<GameEntity> games = gameRepository.searchByQuery(user, query);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id, HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        Optional<GameEntity> game = gameRepository.findByIdAndUser(id, user);
        if (game.isPresent()) {
            return ResponseEntity.ok(game.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Juego no encontrado"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(@PathVariable Long id, @RequestBody GameEntity gameData, HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        Optional<GameEntity> gameOpt = gameRepository.findByIdAndUser(id, user);
        if (gameOpt.isPresent()) {
            GameEntity game = gameOpt.get();
            game.setName(gameData.getName());
            game.setDescription(gameData.getDescription());
            game.setGenre(gameData.getGenre());
            game.setUpdatedAt(LocalDateTime.now());
            
            // Actualizar categorías
            if (gameData.getCategories() != null && !gameData.getCategories().isEmpty()) {
                game.getCategories().clear();
                for (CategoryEntity cat : gameData.getCategories()) {
                    Optional<CategoryEntity> existing = categoryRepository.findByName(cat.getName());
                    if (existing.isPresent()) {
                        game.getCategories().add(existing.get());
                    } else {
                        CategoryEntity newCat = categoryRepository.save(new CategoryEntity(cat.getName()));
                        game.getCategories().add(newCat);
                    }
                }
            }
            
            // Actualizar tags
            if (gameData.getTags() != null && !gameData.getTags().isEmpty()) {
                game.getTags().clear();
                for (TagEntity tag : gameData.getTags()) {
                    Optional<TagEntity> existing = tagRepository.findByName(tag.getName());
                    if (existing.isPresent()) {
                        game.getTags().add(existing.get());
                    } else {
                        TagEntity newTag = tagRepository.save(new TagEntity(tag.getName()));
                        game.getTags().add(newTag);
                    }
                }
            }
            
            // Actualizar secciones
            if (gameData.getSections() != null) {
                game.getSections().clear();
                game.getSections().addAll(gameData.getSections());
            }
            
            gameRepository.save(game);
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Juego no encontrado"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id, HttpSession session) {
        UserEntity user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
        }

        Optional<GameEntity> game = gameRepository.findByIdAndUser(id, user);
        if (game.isPresent()) {
            gameRepository.delete(game.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Juego no encontrado"));
        }
    }
}
