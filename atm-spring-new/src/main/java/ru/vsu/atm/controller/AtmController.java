package ru.vsu.atm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.vsu.atm.domain.enums.AccountType;
import ru.vsu.atm.domain.model.BankAccount;
import ru.vsu.atm.domain.model.User;
import ru.vsu.atm.dto.OperationRequest;
import ru.vsu.atm.service.AccountService;

@RestController
@RequestMapping("/api/atm")
public class AtmController {
    private final AccountService accountService;

    public AtmController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<String> getAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.getBalanceSummary(user.getAccounts()));
    }

    @PostMapping("/open-account")
    public ResponseEntity<String> openAccount(@AuthenticationPrincipal User user, @RequestParam AccountType type) {
        BankAccount account = accountService.openAccount(user, type);
        return ResponseEntity.ok("Account opened: " + account.getAccountType());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@AuthenticationPrincipal User user, @RequestBody OperationRequest req) {
        accountService.deposit(user, req.accountId().intValue(), req.amount());
        return ResponseEntity.ok("Deposited successfully");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal User user, @RequestBody OperationRequest req) {
        accountService.withdraw(user, req.accountId().intValue(), req.amount());
        return ResponseEntity.ok("Withdrawn successfully");
    }
}