package com.adrian.gameconcepthub.domain.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Long id;
    private final String name;
    private final String description;
    private final String genre;
    private final List<Category> categories;
    private final List<Tag> tags;
    private final List<Section> sections;

    public Game(Long id, String name, String description, String genre,
                List<Category> categories, List<Tag> tags, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.categories = categories == null ? Collections.<Category>emptyList() : Collections.unmodifiableList(new ArrayList<Category>(categories));
        this.tags = tags == null ? Collections.<Tag>emptyList() : Collections.unmodifiableList(new ArrayList<Tag>(tags));
        this.sections = sections == null ? Collections.<Section>emptyList() : Collections.unmodifiableList(new ArrayList<Section>(sections));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Section> getSections() {
        return sections;
    }
}
