package ru.vsu.atm.repository;

import org.springframework.stereotype.Repository;
import ru.vsu.atm.domain.entity.UserEntity;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, UserEntity> storage = new ConcurrentHashMap<>();
    private final Map<String, UserEntity> byLogin = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        storage.put(user.getId(), user);
        byLogin.put(user.getLogin(), user);
        return user;
    }

    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<UserEntity> findByLogin(String login) {
        return Optional.ofNullable(byLogin.get(login));
    }

    public boolean existsByLogin(String login) {
        return byLogin.containsKey(login);
    }
}