package main.java.com.adrian.gameconcepthub.config;

import main.java.com.adrian.gameconcepthub.application.service.GameService;
import main.java.com.adrian.gameconcepthub.domain.port.out.CategoryRepository;
import main.java.com.adrian.gameconcepthub.domain.port.out.GameRepository;
import main.java.com.adrian.gameconcepthub.domain.port.out.TagRepository;
import main.java.com.adrian.gameconcepthub.domain.port.out.UserRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaCategoryRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaGameRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaTagRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository.JpaUserRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.web.controller.AuthController;
import main.java.com.adrian.gameconcepthub.infrastructure.web.controller.GameController;

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
