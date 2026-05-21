package ru.vsu.atm.dto.response;

public class UserResponse {
    private Long id;
    private String login;

    public UserResponse(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
}