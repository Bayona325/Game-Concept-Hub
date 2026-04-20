package com.adrian.gameconcepthub.infrastructure.persistence.repository;

import com.adrian.gameconcepthub.domain.model.Tag;
import com.adrian.gameconcepthub.domain.port.out.TagRepository;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.TagEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class JpaTagRepository implements TagRepository {

    private final Map<Long, TagEntity> storage = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Tag save(Tag tag) {
        Optional<Tag> existing = findByName(tag.getName());
        if (existing.isPresent()) {
            return existing.get();
        }

        Long id = tag.getId() == null ? sequence.getAndIncrement() : tag.getId();
        TagEntity entity = new TagEntity(id, tag.getName());
        storage.put(id, entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        String normalized = name.toLowerCase(Locale.ROOT);
        return storage.values().stream()
                .filter(tag -> tag.getName() != null && tag.getName().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst()
                .map(this::toDomain);
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<>();
        for (TagEntity entity : storage.values()) {
            tags.add(toDomain(entity));
        }
        return tags;
    }

    private Tag toDomain(TagEntity entity) {
        return new Tag(entity.getId(), entity.getName());
    }
}
