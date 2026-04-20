package com.adrian.gameconcepthub.domain.port.in;

import com.adrian.gameconcepthub.domain.model.Game;
import java.util.List;

public interface SearchGameUseCase {

    List<Game> search(String query);
}
