package main.java.com.adrian.gameconcepthub;

import main.java.com.adrian.gameconcepthub.config.BeanConfig;
import main.java.com.adrian.gameconcepthub.domain.model.Game;
import java.util.List;

public class GameConceptHubApplication {

    public static void main(String[] args) {
        BeanConfig config = new BeanConfig();
        List<Game> games = config.gameController().search("");
        System.out.println("Game Concept Hub ready. Loaded games: " + games.size());
    }
}
