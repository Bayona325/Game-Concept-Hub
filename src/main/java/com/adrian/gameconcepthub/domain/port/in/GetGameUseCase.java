package com.adrian.gameconcepthub.domain.port.in;

import com.adrian.gameconcepthub.domain.model.Game;
import java.util.Optional;

public interface GetGameUseCase {

    Optional<Game> getGameById(Long id);
}
