package org.example.domain.model;

import org.example.domain.enums.AccountType;

public class DebitAccount extends BankAccount {

    public DebitAccount() {
        super(AccountType.DEBIT, 0); // дебетовый счёт с нулевым балансом
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= getBalance();
    }
}