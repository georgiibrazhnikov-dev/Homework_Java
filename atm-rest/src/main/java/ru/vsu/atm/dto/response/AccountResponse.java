package ru.vsu.atm.dto.response;

import ru.vsu.atm.domain.entity.AccountEntity;

public class AccountResponse {
    private Long id;
    private String type;
    private long balance;
    private long availableBalance;
    private Long creditLine;   // только для кредитного

    public AccountResponse(AccountEntity account) {
        this.id = account.getId();
        this.type = account.getType().name();
        this.balance = account.getBalance();
        this.availableBalance = account.getAvailableBalance();
        if (account.getType() == ru.vsu.atm.domain.enums.AccountType.CREDIT) {
            this.creditLine = account.getCreditLine();
        }
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public long getBalance() { return balance; }
    public long getAvailableBalance() { return availableBalance; }
    public Long getCreditLine() { return creditLine; }
}