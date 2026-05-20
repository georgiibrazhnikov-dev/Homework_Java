package org.example.service;

import org.example.domain.enums.AccountType;
import org.example.domain.model.BankAccount;
import org.example.domain.model.CreditAccount;
import org.example.domain.model.DebitAccount;
import org.example.domain.model.User;
import org.example.exception.AccountNotFoundException;
import org.example.exception.InvalidAmountException;
import org.example.exception.NotEnoughMoneyException;

import java.util.List;

public class AccountService {

    // Открыть новый счёт для пользователя
    public BankAccount openAccount(User user, AccountType type) {
        BankAccount account;
        switch (type) {
            case DEBIT:
                account = new DebitAccount();
                break;
            case CREDIT:
                account = new CreditAccount(20000); // кредитная линия 20000
                break;
            default:
                throw new IllegalArgumentException("Unknown account type");
        }
        user.addAccount(account);
        return account;
    }

    // Пополнение конкретного счёта
    public void deposit(BankAccount account, long amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive, got: " + amount);
        }
        account.setBalance(account.getBalance() + amount);
    }

    // Снятие с конкретного счёта
    public void withdraw(BankAccount account, long amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive, got: " + amount);
        }
        if (!account.canWithdraw(amount)) {
            throw new NotEnoughMoneyException("Not enough money or credit limit exceeded");
        }
        account.setBalance(account.getBalance() - amount);
    }

    // Получение балансов всех счетов пользователя и общей суммы
    // Используем PECS: Producer Extends, Consumer Super.
    // Здесь мы только читаем (getBalance) -> extends, поэтому List<? extends BankAccount>
    public String getBalanceSummary(List<? extends BankAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        long total = 0;
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount acc = accounts.get(i);
            sb.append(i + 1).append(". ").append(acc.getAccountType())
              .append(": ").append(acc.getBalance());
            if (acc instanceof CreditAccount) {
                sb.append(" (credit line: ").append(((CreditAccount) acc).getCreditLine()).append(")");
            }
            sb.append("\n");
            total += acc.getBalance();
        }
        sb.append("Total available: ").append(total);
        return sb.toString();
    }

    // Поиск счёта по индексу (для удобства в меню)
    public BankAccount findAccountByIndex(User user, int index) {
        if (index < 1 || index > user.getAccounts().size()) {
            throw new AccountNotFoundException("Account with index " + index + " not found");
        }
        return user.getAccounts().get(index - 1);
    }
}