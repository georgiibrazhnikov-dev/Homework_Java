package ru.vsu.atm.repository;

import org.springframework.stereotype.Repository;
import ru.vsu.atm.domain.entity.AccountEntity;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepository {
    private final Map<Long, AccountEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public AccountEntity save(AccountEntity account) {
        if (account.getId() == null) {
            account.setId(idGenerator.getAndIncrement());
        }
        storage.put(account.getId(), account);
        return account;
    }

    public Optional<AccountEntity> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}