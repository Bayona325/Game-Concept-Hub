package com.adrian.gameconcepthub.config;

import com.adrian.gameconcepthub.application.service.GameService;
import com.adrian.gameconcepthub.domain.port.out.CategoryRepository;
import com.adrian.gameconcepthub.domain.port.out.GameRepository;
import com.adrian.gameconcepthub.domain.port.out.TagRepository;
import com.adrian.gameconcepthub.domain.port.out.UserRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaCategoryRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaGameRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaTagRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaUserRepository;
import com.adrian.gameconcepthub.infrastructure.web.controller.AuthController;
import com.adrian.gameconcepthub.infrastructure.web.controller.GameController;

public class BeanConfig {

    private final GameRepository gameRepository = new JpaGameRepository();
    private final CategoryRepository categoryRepository = new JpaCategoryRepository();
    private final TagRepository tagRepository = new JpaTagRepository();
    private final UserRepository userRepository = new JpaUserRepository();
    private final GameService gameService = new GameService(gameRepository, categoryRepository, tagRepository);

    public GameRepository gameRepository() {
        return gameRepository;
    }

    public CategoryRepository categoryRepository() {
        return categoryRepository;
    }

    public TagRepository tagRepository() {
        return tagRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public GameService gameService() {
        return gameService;
    }

    public GameController gameController() {
        return new GameController(gameService, gameService, gameService);
    }

    public AuthController authController() {
        return new AuthController(userRepository);
    }
}
