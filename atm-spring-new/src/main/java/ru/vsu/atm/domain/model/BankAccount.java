package ru.vsu.atm.domain.model;

import jakarta.persistence.*;
import ru.vsu.atm.domain.enums.AccountType;

@Entity
@Table(name = "bank_accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type_disc", discriminatorType = DiscriminatorType.STRING)
public abstract class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private long balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type_disc", insertable = false, updatable = false)
    private AccountType accountType;

    protected BankAccount() {}

    public BankAccount(AccountType accountType, long initialBalance) {
        this.accountType = accountType;
        this.balance = initialBalance;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public AccountType getAccountType() { return accountType; }

    public abstract boolean canWithdraw(long amount);
}