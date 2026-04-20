package com.adrian.gameconcepthub.domain.port.out;

import com.adrian.gameconcepthub.domain.model.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    User save(User user);
}
