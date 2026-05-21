package ru.vsu.atm.dto.request;

public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private long amount;

    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
}