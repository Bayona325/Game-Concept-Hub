package com.adrian.gameconcepthub.infrastructure.web.controller;

import com.adrian.gameconcepthub.domain.model.Game;
import com.adrian.gameconcepthub.domain.port.in.CreateGameUseCase;
import com.adrian.gameconcepthub.domain.port.in.GetGameUseCase;
import com.adrian.gameconcepthub.domain.port.in.SearchGameUseCase;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
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

    @PostMapping
    public Game create(@RequestBody Game game) {
        return createGameUseCase.createGame(game);
    }

    @GetMapping("/{id}")
    public Optional<Game> getById(@PathVariable Long id) {
        return getGameUseCase.getGameById(id);
    }

    @GetMapping("/search")
    public List<Game> search(@RequestParam String query) {
        return searchGameUseCase.search(query);
    }
}
