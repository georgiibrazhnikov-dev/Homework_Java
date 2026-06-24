package ru.vsu.atm.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BankAccount> accounts = new ArrayList<>();

    protected User() {} // Обязательно для JPA

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public List<BankAccount> getAccounts() { return accounts; }

    public void addAccount(BankAccount account) {
        accounts.add(account);
        account.setUser(this);
    }
}