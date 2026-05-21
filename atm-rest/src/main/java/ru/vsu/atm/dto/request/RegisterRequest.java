package ru.vsu.atm.dto.request;

public class RegisterRequest {
    private String login;
    private String password;

    // геттеры, сеттеры
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}