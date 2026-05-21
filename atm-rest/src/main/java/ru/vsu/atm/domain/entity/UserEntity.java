package ru.vsu.atm.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private Long id;
    private String login;
    private String password;
    private List<AccountEntity> accounts = new ArrayList<>();

    public UserEntity(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public List<AccountEntity> getAccounts() { return accounts; }
    public void addAccount(AccountEntity account) { accounts.add(account); }
}