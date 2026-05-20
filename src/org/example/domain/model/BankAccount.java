package org.example.domain.model;

import org.example.domain.enums.AccountType;

public abstract class BankAccount {
    private final AccountType accountType;
    private long balance; // у кредитного это будет отрицательный остаток (или лимит)

    public BankAccount(AccountType accountType, long initialBalance) {
        this.accountType = accountType;
        this.balance = initialBalance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public long getBalance() {
        return balance;
    }

    // Методы для изменения баланса (будут использоваться через AccountService)
    public void setBalance(long balance) {
        this.balance = balance;
    }

    // Абстрактные методы — наследники реализуют специфику
    public abstract boolean canWithdraw(long amount);
}