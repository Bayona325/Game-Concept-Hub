package com.adrian.gameconcepthub.application.service;

import com.adrian.gameconcepthub.domain.model.Category;
import com.adrian.gameconcepthub.domain.model.Game;
import com.adrian.gameconcepthub.domain.model.Tag;
import com.adrian.gameconcepthub.domain.port.in.CreateGameUseCase;
import com.adrian.gameconcepthub.domain.port.in.GetGameUseCase;
import com.adrian.gameconcepthub.domain.port.in.SearchGameUseCase;
import com.adrian.gameconcepthub.domain.port.out.CategoryRepository;
import com.adrian.gameconcepthub.domain.port.out.GameRepository;
import com.adrian.gameconcepthub.domain.port.out.TagRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameService implements CreateGameUseCase, GetGameUseCase, SearchGameUseCase {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public GameService(GameRepository gameRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository) {
        this.gameRepository = Objects.requireNonNull(gameRepository);
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.tagRepository = Objects.requireNonNull(tagRepository);
    }

    @Override
    public Game createGame(Game game) {
        validate(game);

        List<Category> persistedCategories = game.getCategories().stream()
                .map(categoryRepository::save)
                .collect(Collectors.toList());

        List<Tag> persistedTags = game.getTags().stream()
                .map(tagRepository::save)
                .collect(Collectors.toList());

        Game normalizedGame = new Game(
                game.getId(),
                game.getName().trim(),
                game.getDescription(),
                game.getGenre(),
                persistedCategories,
                persistedTags,
                game.getSections()
        );

        return gameRepository.save(normalizedGame);
    }

    @Override
    public Optional<Game> getGameById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return gameRepository.findAll();
        }
        return gameRepository.searchByName(query.trim());
    }

    private void validate(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (game.getName() == null || game.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Game name is required");
        }
        if (game.getGenre() == null || game.getGenre().trim().isEmpty()) {
            throw new IllegalArgumentException("Game genre is required");
        }
    }
}
