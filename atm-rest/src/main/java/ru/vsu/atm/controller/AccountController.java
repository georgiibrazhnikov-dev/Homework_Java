package ru.vsu.atm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.atm.domain.entity.AccountEntity;
import ru.vsu.atm.domain.entity.UserEntity;
import ru.vsu.atm.domain.enums.AccountType;
import ru.vsu.atm.dto.request.*;
import ru.vsu.atm.dto.response.AccountResponse;
import ru.vsu.atm.dto.response.SummaryResponse;
import ru.vsu.atm.dto.response.TransferResponse;
import ru.vsu.atm.service.AccountService;
import ru.vsu.atm.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    // 3.1 Создать счёт
    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long userId,
                                                         @RequestBody CreateAccountRequest request) {
        AccountType type = AccountType.valueOf(request.getType().toUpperCase());
        AccountEntity account = accountService.openAccount(userId, type);
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // 3.2 Получить все счета пользователя
    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable Long userId) {
        List<AccountEntity> accounts = accountService.getUserAccounts(userId);
        List<AccountResponse> response = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 3.3 Получить конкретный счёт
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        AccountEntity account = accountService.getAccount(accountId);
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // 3.4 Пополнить счёт
    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable Long accountId,
                                                   @RequestBody DepositRequest request) {
        AccountEntity account = accountService.deposit(accountId, request.getAmount());
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // 3.5 Снять деньги
    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable Long accountId,
                                                    @RequestBody WithdrawRequest request) {
        AccountEntity account = accountService.withdraw(accountId, request.getAmount());
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // 3.6 Перевод денег
    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        TransferResponse response = new TransferResponse(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                "SUCCESS"
        );
        return ResponseEntity.ok(response);
    }

    // 4. Общая доступная сумма по всем счетам пользователя
    @GetMapping("/users/{userId}/accounts/summary")
    public ResponseEntity<SummaryResponse> getSummary(@PathVariable Long userId) {
        UserEntity user = userService.findById(userId);
        long totalBalance = accountService.getTotalBalance(user);
        long totalAvailable = accountService.getTotalAvailableBalance(user);
        int count = user.getAccounts().size();
        SummaryResponse summary = new SummaryResponse(userId, totalBalance, totalAvailable, count);
        return ResponseEntity.ok(summary);
    }
}