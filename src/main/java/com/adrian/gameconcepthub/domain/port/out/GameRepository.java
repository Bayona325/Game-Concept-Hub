package main.java.com.adrian.gameconcepthub.domain.port.out;

import main.java.com.adrian.gameconcepthub.domain.model.Game;
import java.util.List;
import java.util.Optional;

public interface GameRepository {

    Game save(Game game);

    Optional<Game> findById(Long id);

    List<Game> findAll();

    List<Game> searchByName(String name);

    void deleteById(Long id);
}
