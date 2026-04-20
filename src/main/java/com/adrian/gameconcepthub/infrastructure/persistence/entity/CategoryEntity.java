package com.adrian.gameconcepthub.infrastructure.persistence.entity;

public class CategoryEntity {

    private Long id;
    private String name;
    private CategoryEntity parent;

    public CategoryEntity() {
    }

    public CategoryEntity(Long id, String name, CategoryEntity parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
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

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }
}
