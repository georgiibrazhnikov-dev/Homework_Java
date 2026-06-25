package ru.vsu.atm.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.vsu.atm.domain.enums.AccountType;

@Entity
@DiscriminatorValue("DEBIT")
public class DebitAccount extends BankAccount {
    public DebitAccount() { super(AccountType.DEBIT, 0); }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= getBalance();
    }
}