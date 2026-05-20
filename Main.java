package bankomat;

public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("Georgii Brazhnikov", 10000);
        ATM atm = new ATM(account);
        atm.showMenu();
    }
}  