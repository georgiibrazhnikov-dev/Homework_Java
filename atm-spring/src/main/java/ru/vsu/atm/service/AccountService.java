package ru.vsu.atm.service;

import org.springframework.stereotype.Service;
import ru.vsu.atm.domain.enums.AccountType;
import ru.vsu.atm.domain.model.BankAccount;
import ru.vsu.atm.domain.model.CreditAccount;
import ru.vsu.atm.domain.model.DebitAccount;
import ru.vsu.atm.domain.model.User;
import ru.vsu.atm.exception.*;

import java.util.List;

@Service
public class AccountService {

    public BankAccount openAccount(User user, AccountType type) {
        BankAccount account;
        switch (type) {
            case DEBIT:
                account = new DebitAccount();
                break;
            case CREDIT:
                account = new CreditAccount(20000);
                break;
            default:
                throw new IllegalArgumentException("Unknown account type");
        }
        user.addAccount(account);
        return account;
    }

    public void deposit(BankAccount account, long amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        account.setBalance(account.getBalance() + amount);
    }

    public void withdraw(BankAccount account, long amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        if (!account.canWithdraw(amount))
            throw new NotEnoughMoneyException("Not enough money or credit limit exceeded");
        account.setBalance(account.getBalance() - amount);
    }

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

    public BankAccount findAccountByIndex(User user, int index) {
        if (index < 1 || index > user.getAccounts().size())
            throw new AccountNotFoundException("Account with index " + index + " not found");
        return user.getAccounts().get(index - 1);
    }
}