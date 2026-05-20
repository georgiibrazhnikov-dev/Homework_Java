package bankomat;

import java.util.Scanner;

public class ATM {
    private BankAccount account;
    private Scanner scanner;

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n[Меню]");
            System.out.println("1.Проверить баланс");
            System.out.println("2.Пополнить счёт");
            System.out.println("3.Снять деньги");
            System.out.println("4.Выйти из системы");
            System.out.print("Выберите услугу: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println("До свидания!");
                    running = false;
                    break;
                default:
                    System.out.println("Неккоректная опция.");
            }
        }
        scanner.close();
    }

    private void checkBalance() {
        System.out.println("Ваш баланс: " + account.getBalance());
    }

    private void deposit() {
        System.out.print("Сколько средств хотите внести: ");
        long amount = scanner.nextLong();
        scanner.nextLine();
        if (amount > 0) {
            account.deposit(amount);
            System.out.println("Успешно. Ваш баланс: " + account.getBalance());
        } else {
            System.out.println("Некорректные данные.");
        }
    }

    private void withdraw() {
        System.out.print("Введите сумму для снятитя с счёта: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        if (account.withdraw(amount)) {
            dispenseMoney(amount);
            System.out.println("Возьмите деньги.");
        } else {
            System.out.println("НЕдостаточно средств или некорректная сумма.");
        }
    }

    private void dispenseMoney(long amount) {
        System.out.println("Выдача средств:");
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