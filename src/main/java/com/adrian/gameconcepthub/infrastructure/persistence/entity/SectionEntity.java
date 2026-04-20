package com.adrian.gameconcepthub.infrastructure.persistence.entity;

import com.adrian.gameconcepthub.domain.model.SectionType;

public class SectionEntity {

    private Long id;
    private String title;
    private String content;
    private SectionType type;

    public SectionEntity() {
    }

    public SectionEntity(Long id, String title, String content, SectionType type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SectionType getType() {
        return type;
    }

    public void setType(SectionType type) {
        this.type = type;
    }
}
