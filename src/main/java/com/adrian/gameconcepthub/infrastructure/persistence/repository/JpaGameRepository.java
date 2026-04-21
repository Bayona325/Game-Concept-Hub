package com.adrian.gameconcepthub.infrastructure.persistence.repository;

import com.adrian.gameconcepthub.infrastructure.persistence.entity.GameEntity;
import com.adrian.gameconcepthub.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaGameRepository extends JpaRepository<GameEntity, Long> {
    
    List<GameEntity> findByUser(UserEntity user);
    
    List<GameEntity> findByUserAndNameContainingIgnoreCase(UserEntity user, String name);
    
    List<GameEntity> findByUserAndGenreContainingIgnoreCase(UserEntity user, String genre);
    
    Optional<GameEntity> findByIdAndUser(Long id, UserEntity user);
    
    @Query("SELECT g FROM GameEntity g WHERE g.user = :user AND (" +
           "LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.genre) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<GameEntity> searchByQuery(@Param("user") UserEntity user, @Param("query") String query);
}
