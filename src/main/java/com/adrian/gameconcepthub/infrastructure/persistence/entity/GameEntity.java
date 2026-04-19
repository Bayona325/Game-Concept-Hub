package main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

public class GameEntity {

    private Long id;
    private String name;
    private String description;
    private String genre;
    private List<CategoryEntity> categories = new ArrayList<>();
    private List<TagEntity> tags = new ArrayList<>();
    private List<SectionEntity> sections = new ArrayList<>();

    public GameEntity() {
    }

    public GameEntity(Long id, String name, String description, String genre,
                      List<CategoryEntity> categories, List<TagEntity> tags, List<SectionEntity> sections) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.categories = categories == null ? new ArrayList<>() : new ArrayList<>(categories);
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
        this.sections = sections == null ? new ArrayList<>() : new ArrayList<>(sections);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories == null ? new ArrayList<>() : new ArrayList<>(categories);
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public List<SectionEntity> getSections() {
        return sections;
    }

    public void setSections(List<SectionEntity> sections) {
        this.sections = sections == null ? new ArrayList<>() : new ArrayList<>(sections);
    }
}
