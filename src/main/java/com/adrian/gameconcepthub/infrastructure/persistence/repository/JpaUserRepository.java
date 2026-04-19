package main.java.com.adrian.gameconcepthub.infrastructure.persistence.repository;

import main.java.com.adrian.gameconcepthub.domain.model.User;
import main.java.com.adrian.gameconcepthub.domain.port.out.UserRepository;
import main.java.com.adrian.gameconcepthub.infrastructure.persistence.entity.UserEntity;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class JpaUserRepository implements UserRepository {

    private final Map<String, UserEntity> storage = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(storage.get(username)).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        Long id = user.getId() == null ? sequence.getAndIncrement() : user.getId();
        UserEntity entity = new UserEntity(id, user.getUsername(), user.getPassword(), user.getRole());
        storage.put(entity.getUsername(), entity);
        return toDomain(entity);
    }

    private User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
    }
}
