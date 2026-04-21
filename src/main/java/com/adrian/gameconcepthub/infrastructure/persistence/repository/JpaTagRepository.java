package com.adrian.gameconcepthub.infrastructure.persistence.repository;

import com.adrian.gameconcepthub.infrastructure.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
    boolean existsByName(String name);
}
