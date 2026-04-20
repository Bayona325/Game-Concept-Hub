package com.adrian.gameconcepthub.domain.model;

public class Section {

    private final Long id;
    private final String title;
    private final String content;
    private final SectionType type;

    public Section(Long id, String title, String content, SectionType type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public SectionType getType() {
        return type;
    }
}
