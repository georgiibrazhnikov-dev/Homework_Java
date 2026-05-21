package ru.vsu.atm.dto.request;

public class CreateAccountRequest {
    private String type; // "DEBIT" или "CREDIT"

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}