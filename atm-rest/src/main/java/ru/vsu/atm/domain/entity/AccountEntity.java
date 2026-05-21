package ru.vsu.atm.domain.entity;

import ru.vsu.atm.domain.enums.AccountType;

public class AccountEntity {
    private Long id;
    private AccountType type;
    private long balance;           // текущий баланс (для кредитного может быть отрицательным)
    private long creditLine;        // 0 для дебетового

    public AccountEntity(Long id, AccountType type, long creditLine) {
        this.id = id;
        this.type = type;
        this.creditLine = creditLine;
        this.balance = 0;
    }

    // геттеры и сеттеры
    public Long getId() { return id; }
    public AccountType getType() { return type; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public long getCreditLine() { return creditLine; }

    // доступный остаток (баланс + кредитная линия, но не меньше 0)
    public long getAvailableBalance() {
        if (type == AccountType.CREDIT) {
            return creditLine + balance; // balance может быть отрицательным
        } else {
            return balance;
        }
    }

    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= getAvailableBalance();
    }
}