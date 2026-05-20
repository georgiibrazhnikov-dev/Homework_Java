package org.example.controller;

import org.example.domain.enums.AccountType;
import org.example.domain.enums.Denomination;
import org.example.domain.model.BankAccount;
import org.example.domain.model.User;
import org.example.exception.*;
import org.example.service.AccountService;
import org.example.service.UserService;

import java.util.Scanner;

public class ATM {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;
    private User currentUser;

    public ATM(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");
        currentUser = authenticate(); // сначала авторизация/регистрация
        mainMenu();
    }

    private User authenticate() {
        while (true) {
            System.out.println("\n1. Sign in");
            System.out.println("2. Sign up");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    return signIn();
                case 2:
                    return signUp();
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private User signIn() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            User user = userService.login(login, password);
            System.out.println("Login successful. Welcome, " + user.getLogin() + "!");
            return user;
        } catch (UserLoginNotFoundException | InvalidPasswordException e) {
            System.out.println("Error: " + e.getMessage());
            return authenticate(); // повтор
        }
    }

    private User signUp() {
        System.out.print("Choose login: ");
        String login = scanner.nextLine();
        System.out.print("Choose password: ");
        String password = scanner.nextLine();
        try {
            User user = userService.register(login, password);
            System.out.println("Registration successful. Welcome, " + user.getLogin() + "!");
            return user;
        } catch (UserAlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
            return authenticate(); // повтор
        }
    }

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ATM MENU ===");
            System.out.println("1. Check balance");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Open new account");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        checkBalance();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        openAccount();
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InvalidAmountException | NotEnoughMoneyException | AccountNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void checkBalance() {
        if (currentUser.getAccounts().isEmpty()) {
            System.out.println("No accounts yet. Please open an account first.");
            return;
        }
        String summary = accountService.getBalanceSummary(currentUser.getAccounts());
        System.out.println("Your accounts:\n" + summary);
    }

    private void deposit() {
        BankAccount account = selectAccount();
        System.out.print("Enter amount to deposit: ");
        long amount = scanner.nextLong();
        scanner.nextLine();
        accountService.deposit(account, amount);
        System.out.println("Deposit successful. New balance: " + account.getBalance());
    }

    private void withdraw() {
        BankAccount account = selectAccount();
        System.out.print("How much to withdraw? ");
        long amount = scanner.nextLong();
        scanner.nextLine();
        accountService.withdraw(account, amount);
        dispenseMoney(amount);
        System.out.println("Please take your money.");
    }

    private void openAccount() {
        System.out.println("Choose account type:");
        System.out.println("1. Debit (balance = 0)");
        System.out.println("2. Credit (credit line = 20000)");
        int type = scanner.nextInt();
        scanner.nextLine();
        AccountType accountType;
        switch (type) {
            case 1:
                accountType = AccountType.DEBIT;
                break;
            case 2:
                accountType = AccountType.CREDIT;
                break;
            default:
                System.out.println("Invalid type.");
                return;
        }
        BankAccount account = accountService.openAccount(currentUser, accountType);
        System.out.println("Account opened: " + account.getAccountType() + ", balance: " + account.getBalance());
    }

    private BankAccount selectAccount() {
        if (currentUser.getAccounts().isEmpty()) {
            throw new AccountNotFoundException("No accounts available");
        }
        System.out.println("Select account:");
        for (int i = 0; i < currentUser.getAccounts().size(); i++) {
            BankAccount acc = currentUser.getAccounts().get(i);
            System.out.println((i + 1) + ". " + acc.getAccountType() + " (balance: " + acc.getBalance() + ")");
        }
        System.out.print("Enter account number: ");
        int index = scanner.nextInt();
        scanner.nextLine();
        return accountService.findAccountByIndex(currentUser, index);
    }

    private void dispenseMoney(long amount) {
        System.out.println("Dispensing:");
        long remaining = amount;
        for (Denomination denom : Denomination.values()) {
            int count = (int) (remaining / denom.getValue());
            if (count > 0) {
                System.out.println(denom.getValue() + " x " + count);
                remaining -= count * denom.getValue();
            }
        }
    }
}