package org.example.domain.model;

import org.example.domain.enums.AccountType;

public class CreditAccount extends BankAccount {
    private final long creditLine; // доступный кредитный лимит (положительное число)

    public CreditAccount(long creditLine) {
        super(AccountType.CREDIT, 0); // начальный баланс = 0, но снимать можно в пределах кредитной линии
        this.creditLine = creditLine;
    }

    public long getCreditLine() {
        return creditLine;
    }

    @Override
    public boolean canWithdraw(long amount) {
        // Можно снять, если сумма положительна и не превышает (текущий баланс + кредитная линия)
        // Баланс может быть отрицательным (задолженность), поэтому доступно = creditLine - использовано
        long available = creditLine + getBalance(); // getBalance() отрицателен, если мы должны банку
        return amount > 0 && amount <= available;
    }
}