package ru.vsu.atm.domain.model;

import ru.vsu.atm.domain.enums.AccountType;

public class CreditAccount extends BankAccount {
    private final long creditLine;

    public CreditAccount(long creditLine) {
        super(AccountType.CREDIT, 0);
        this.creditLine = creditLine;
    }

    public long getCreditLine() {
        return creditLine;
    }

    @Override
    public boolean canWithdraw(long amount) {
        long available = creditLine + getBalance(); // getBalance() отрицателен при задолженности
        return amount > 0 && amount <= available;
    }
}