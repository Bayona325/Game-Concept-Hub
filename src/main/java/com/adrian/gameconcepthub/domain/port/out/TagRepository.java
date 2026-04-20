package com.adrian.gameconcepthub.domain.port.out;

import com.adrian.gameconcepthub.domain.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findByName(String name);

    List<Tag> findAll();
}
