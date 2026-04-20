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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public GameRepository gameRepository() {
        return new JpaGameRepository();
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return new JpaCategoryRepository();
    }

    @Bean
    public TagRepository tagRepository() {
        return new JpaTagRepository();
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository();
    }

    @Bean
    public GameService gameService(GameRepository gameRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        return new GameService(gameRepository, categoryRepository, tagRepository);
    }

    @Bean
    public GameController gameController(GameService gameService) {
        return new GameController(gameService, gameService, gameService);
    }

    @Bean
    public AuthController authController(UserRepository userRepository) {
        return new AuthController(userRepository);
    }
}
