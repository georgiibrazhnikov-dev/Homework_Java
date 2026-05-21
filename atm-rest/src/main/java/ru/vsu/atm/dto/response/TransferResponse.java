package ru.vsu.atm.dto.response;

public class TransferResponse {
    private Long fromAccountId;
    private Long toAccountId;
    private long amount;
    private String status;

    public TransferResponse(Long from, Long to, long amount, String status) {
        this.fromAccountId = from;
        this.toAccountId = to;
        this.amount = amount;
        this.status = status;
    }

    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public long getAmount() { return amount; }
    public String getStatus() { return status; }
}