package main.java.com.adrian.gameconcepthub.infrastructure.web.controller;

import main.java.com.adrian.gameconcepthub.domain.model.Game;
import main.java.com.adrian.gameconcepthub.domain.port.in.CreateGameUseCase;
import main.java.com.adrian.gameconcepthub.domain.port.in.GetGameUseCase;
import main.java.com.adrian.gameconcepthub.domain.port.in.SearchGameUseCase;
import java.util.List;
import java.util.Optional;

public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final GetGameUseCase getGameUseCase;
    private final SearchGameUseCase searchGameUseCase;

    public GameController(CreateGameUseCase createGameUseCase,
                          GetGameUseCase getGameUseCase,
                          SearchGameUseCase searchGameUseCase) {
        this.createGameUseCase = createGameUseCase;
        this.getGameUseCase = getGameUseCase;
        this.searchGameUseCase = searchGameUseCase;
    }

    public Game create(Game game) {
        return createGameUseCase.createGame(game);
    }

    public Optional<Game> getById(Long id) {
        return getGameUseCase.getGameById(id);
    }

    public List<Game> search(String query) {
        return searchGameUseCase.search(query);
    }
}
