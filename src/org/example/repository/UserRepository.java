package org.example.repository;

import org.example.domain.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final Map<String, User> storage = new HashMap<>();

    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(storage.get(login));
    }

    public void save(User user) {
        storage.put(user.getLogin(), user);
    }

    public boolean existsByLogin(String login) {
        return storage.containsKey(login);
    }
}