package com.adrian.gameconcepthub.infrastructure.persistence.repository;

import com.adrian.gameconcepthub.domain.model.Category;
import com.adrian.gameconcepthub.domain.port.out.CategoryRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.CategoryEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class JpaCategoryRepository implements CategoryRepository {

    private final Map<Long, CategoryEntity> storage = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Category save(Category category) {
        Long id = category.getId() == null ? sequence.getAndIncrement() : category.getId();
        CategoryEntity entity = toEntity(new Category(id, category.getName(), category.getParent()));
        storage.put(id, entity);
        return toDomain(entity);
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity entity : storage.values()) {
            categories.add(toDomain(entity));
        }
        return categories;
    }

    private CategoryEntity toEntity(Category category) {
        CategoryEntity parent = category.getParent() == null ? null : toEntity(category.getParent());
        return new CategoryEntity(category.getId(), category.getName(), parent);
    }

    private Category toDomain(CategoryEntity entity) {
        Category parent = entity.getParent() == null ? null : toDomain(entity.getParent());
        return new Category(entity.getId(), entity.getName(), parent);
    }
}
