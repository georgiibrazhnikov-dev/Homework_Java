package ru.vsu.atm.domain.model;

import ru.vsu.atm.domain.enums.AccountType;

public abstract class BankAccount {
    private final AccountType accountType;
    private long balance;

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

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public abstract boolean canWithdraw(long amount);
}