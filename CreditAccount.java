package ru.vsu.atm.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.vsu.atm.domain.enums.AccountType;

@Entity
@DiscriminatorValue("CREDIT")
public class CreditAccount extends BankAccount {
    private long creditLine;

    protected CreditAccount() { 
        super(AccountType.CREDIT, 0); 
    } // Пустой конструктор для JPA

    public CreditAccount(long creditLine) {
        super(AccountType.CREDIT, 0);
        this.creditLine = creditLine;
    }

    public long getCreditLine() { 
        return creditLine; 
    }

    @Override
    public boolean canWithdraw(long amount) {
        long available = creditLine + getBalance();
        return amount > 0 && amount <= available;
    }
}