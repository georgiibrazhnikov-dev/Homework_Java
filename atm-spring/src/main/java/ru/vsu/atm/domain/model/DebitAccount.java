package ru.vsu.atm.domain.model;

import ru.vsu.atm.domain.enums.AccountType;

public class DebitAccount extends BankAccount {

    public DebitAccount() {
        super(AccountType.DEBIT, 0);
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= getBalance();
    }
}