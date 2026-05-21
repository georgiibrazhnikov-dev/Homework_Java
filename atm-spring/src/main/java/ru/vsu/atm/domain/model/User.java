package ru.vsu.atm.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String login;
    private final String password;
    private final List<BankAccount> accounts = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }
}