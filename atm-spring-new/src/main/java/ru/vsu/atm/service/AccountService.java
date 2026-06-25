package ru.vsu.atm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.atm.domain.enums.AccountType;
import ru.vsu.atm.domain.model.BankAccount;
import ru.vsu.atm.domain.model.CreditAccount;
import ru.vsu.atm.domain.model.DebitAccount;
import ru.vsu.atm.domain.model.User;
import ru.vsu.atm.exception.*;
import ru.vsu.atm.repository.UserRepository;
import java.util.List;

@Service
public class AccountService {

    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public BankAccount openAccount(User user, AccountType type) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BankAccount account = (type == AccountType.DEBIT) ? new DebitAccount() : new CreditAccount(20000);
        managedUser.addAccount(account);
        return account;
    }

    @Transactional
    public void deposit(User user, int accountIndex, long amount) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BankAccount account = findAccountByIndex(managedUser, accountIndex);
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        account.setBalance(account.getBalance() + amount);
    }

    @Transactional
    public void withdraw(User user, int accountIndex, long amount) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BankAccount account = findAccountByIndex(managedUser, accountIndex);
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        if (!account.canWithdraw(amount)) throw new NotEnoughMoneyException("Not enough money");
        account.setBalance(account.getBalance() - amount);
    }

    public String getBalanceSummary(List<? extends BankAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        long total = 0;
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount acc = accounts.get(i);
            sb.append(i + 1).append(". ").append(acc.getAccountType()).append(": ").append(acc.getBalance());
            if (acc instanceof CreditAccount) {
                sb.append(" (credit line: ").append(((CreditAccount) acc).getCreditLine()).append(")");
            }
            sb.append("\n");
            total += acc.getBalance();
        }
        sb.append("Total available: ").append(total);
        return sb.toString();
    }

    private BankAccount findAccountByIndex(User user, int index) {
        if (index < 1 || index > user.getAccounts().size())
            throw new AccountNotFoundException("Account not found");
        return user.getAccounts().get(index - 1);
    }
}