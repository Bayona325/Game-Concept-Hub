package com.adrian.gameconcepthub.domain.port.in;

import com.adrian.gameconcepthub.domain.model.Game;

public interface CreateGameUseCase {

    Game createGame(Game game);
}
