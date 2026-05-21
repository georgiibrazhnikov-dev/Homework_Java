package ru.vsu.atm.service;

import org.springframework.stereotype.Service;
import ru.vsu.atm.domain.entity.AccountEntity;
import ru.vsu.atm.domain.entity.UserEntity;
import ru.vsu.atm.domain.enums.AccountType;
import ru.vsu.atm.exception.AccountNotFoundException;
import ru.vsu.atm.exception.InvalidAmountException;
import ru.vsu.atm.exception.NotEnoughMoneyException;
import ru.vsu.atm.repository.AccountRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public AccountEntity openAccount(Long userId, AccountType type) {
        UserEntity user = userService.findById(userId);
        long creditLine = (type == AccountType.CREDIT) ? 20000 : 0;
        AccountEntity account = new AccountEntity(null, type, creditLine);
        account = accountRepository.save(account);
        user.addAccount(account);
        return account;
    }

    public AccountEntity deposit(Long accountId, long amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        AccountEntity account = getAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        return account;
    }

    public AccountEntity withdraw(Long accountId, long amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        AccountEntity account = getAccount(accountId);
        if (!account.canWithdraw(amount)) {
            throw new NotEnoughMoneyException("Not enough money or credit limit exceeded");
        }
        account.setBalance(account.getBalance() - amount);
        return account;
    }

    public void transfer(Long fromAccountId, Long toAccountId, long amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        AccountEntity from = getAccount(fromAccountId);
        AccountEntity to = getAccount(toAccountId);
        if (!from.canWithdraw(amount)) {
            throw new NotEnoughMoneyException("Insufficient funds for transfer");
        }
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
    }

    public AccountEntity getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public List<AccountEntity> getUserAccounts(Long userId) {
        UserEntity user = userService.findById(userId);
        return user.getAccounts();
    }

    // для summary
    public long getTotalBalance(UserEntity user) {
        return user.getAccounts().stream().mapToLong(AccountEntity::getBalance).sum();
    }

    public long getTotalAvailableBalance(UserEntity user) {
        return user.getAccounts().stream().mapToLong(AccountEntity::getAvailableBalance).sum();
    }
}