package main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository;

import main.java.com.adrian.gameconcepthub.domain.model.Category;
import main.java.com.adrian.gameconcepthub.domain.model.Game;
import main.java.com.adrian.gameconcepthub.domain.model.Section;
import main.java.com.adrian.gameconcepthub.domain.model.Tag;
import main.java.com.adrian.gameconcepthub.domain.port.out.GameRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity.CategoryEntity;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity.GameEntity;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity.SectionEntity;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity.TagEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JpaGameRepository implements GameRepository {

    private final Map<Long, GameEntity> storage = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Game save(Game game) {
        Long id = game.getId() == null ? sequence.getAndIncrement() : game.getId();
        GameEntity entity = toEntity(new Game(
                id,
                game.getName(),
                game.getDescription(),
                game.getGenre(),
                game.getCategories(),
                game.getTags(),
                game.getSections()
        ));
        storage.put(id, entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Game> findById(Long id) {
        return Optional.ofNullable(storage.get(id)).map(this::toDomain);
    }

    @Override
    public List<Game> findAll() {
        List<Game> games = new ArrayList<>();
        for (GameEntity entity : storage.values()) {
            games.add(toDomain(entity));
        }
        return games;
    }

    @Override
    public List<Game> searchByName(String name) {
        String normalized = name.toLowerCase(Locale.ROOT);
        return storage.values().stream()
                .filter(entity -> entity.getName() != null
                        && entity.getName().toLowerCase(Locale.ROOT).contains(normalized))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    private GameEntity toEntity(Game game) {
        List<CategoryEntity> categories = game.getCategories().stream()
                .map(this::toCategoryEntity)
                .collect(Collectors.toList());
        List<TagEntity> tags = game.getTags().stream()
                .map(tag -> new TagEntity(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        List<SectionEntity> sections = game.getSections().stream()
                .map(section -> new SectionEntity(section.getId(), section.getTitle(), section.getContent(), section.getType()))
                .collect(Collectors.toList());

        return new GameEntity(game.getId(), game.getName(), game.getDescription(), game.getGenre(), categories, tags, sections);
    }

    private Game toDomain(GameEntity entity) {
        List<Category> categories = entity.getCategories().stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
        List<Tag> tags = entity.getTags().stream()
                .map(tag -> new Tag(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        List<Section> sections = entity.getSections().stream()
                .map(section -> new Section(section.getId(), section.getTitle(), section.getContent(), section.getType()))
                .collect(Collectors.toList());

        return new Game(entity.getId(), entity.getName(), entity.getDescription(), entity.getGenre(), categories, tags, sections);
    }

    private CategoryEntity toCategoryEntity(Category category) {
        CategoryEntity parent = category.getParent() == null ? null : toCategoryEntity(category.getParent());
        return new CategoryEntity(category.getId(), category.getName(), parent);
    }

    private Category toCategory(CategoryEntity entity) {
        Category parent = entity.getParent() == null ? null : toCategory(entity.getParent());
        return new Category(entity.getId(), entity.getName(), parent);
    }
}
