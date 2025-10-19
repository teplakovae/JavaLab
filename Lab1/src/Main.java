import java.util.List;
import java.util.Scanner;

/**
 * Главный класс приложения Bank System
 * Содержит меню и управление
 */
public class Main {
    private static BankSystem bankSystem = new BankSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Bank System ===");

        addTestData();
        System.out.println("Test data uploaded\n");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Select an action: ");

            switch (choice) {
                case 1: openAccountMenu(); break;
                case 2: topUpMenu(); break;
                case 3: withdrawMenu(); break;
                case 4: balanceMenu(); break;
                case 5: transactionsMenu(); break;
                case 6: searchMenu(); break;
                case 7: showAllAccountsMenu(); break;
                case 8: saveToFileMenu(); break;
                case 9: convertMoneyMenu(); break;
                case 10: showRatesMenu(); break;
                case 0:
                    running = false;
                    System.out.println("Log out of the system...");
                    break;
                default:
                    System.out.println("Wrong choice!");
            }
        }

        scanner.close();
    }

    //Добавляет тестовые данные для демонстрации
    private static void addTestData() {
        bankSystem.openAccount("1234567890", "044525225", "Ivan Ivanov", "1234567890", "123456789");
        bankSystem.openAccount("0987654321", "044525999", "Petr Petrov", "0987654321", "987654321");
        bankSystem.openAccount("1112223334", "044525225", "Maria Sidorova", "1234567890", "123456789");
        bankSystem.openAccount("5556667778", "044525888", "Anna Kozlova", "5556667778", "555666777");

        bankSystem.topUp("1234567890", 5000);
        bankSystem.withdraw("1234567890", 1000);
        bankSystem.topUp("0987654321", 10000);
        bankSystem.topUp("1112223334", 7500);
        bankSystem.withdraw("1112223334", 2000);
        bankSystem.topUp("5556667778", 3000);
    }

    private static void printMenu() {
        System.out.println("\n--- Main menu ---");
        System.out.println("1. Open account");
        System.out.println("2. Top-up");
        System.out.println("3. Withdraw");
        System.out.println("4. Show balance");
        System.out.println("5. Operation history");
        System.out.println("6. Search account by attributes");
        System.out.println("7. Show all accounts");
        System.out.println("8. Save operations to file");
        System.out.println("9. Convert currency");
        System.out.println("10. Show exchange rates");
        System.out.println("0. Exit");
    }

    //Меню поиска счетов по различным атрибутам
    private static void searchMenu() {
        System.out.println("\n--- Search accounts by attributes ---");
        System.out.println("1. Search by account number");
        System.out.println("2. Search by BIC");
        System.out.println("3. Search by name");
        System.out.println("4. Search by INN");
        System.out.println("5. Search by KPP");
        System.out.println("6. Universal search by all attributes");
        System.out.println("0. Back");

        int searchChoice = getIntInput("Choose search type: ");

        if (searchChoice == 0) return;

        String searchText = getStringInput("Enter data for search: ");
        List<BankAccount> foundAccounts;

        //Тип поиска в зависимости от выбора пользователя
        switch (searchChoice) {
            case 1:
                foundAccounts = bankSystem.searchByAccountNumber(searchText);
                System.out.println("Search results by account number:");
                break;
            case 2:
                foundAccounts = bankSystem.searchByBIC(searchText);
                System.out.println("Search results by BIC:");
                break;
            case 3:
                foundAccounts = bankSystem.searchByName(searchText);
                System.out.println("Search results by name:");
                break;
            case 4:
                foundAccounts = bankSystem.searchByINN(searchText);
                System.out.println("Search results by INN:");
                break;
            case 5:
                foundAccounts = bankSystem.searchByKPP(searchText);
                System.out.println("Search results by KPP:");
                break;
            case 6:
                foundAccounts = bankSystem.searchAccounts(searchText);
                System.out.println("Universal search results:");
                break;
            default:
                System.out.println("Wrong choice!");
                return;
        }

        showSearchResults(foundAccounts);
    }


    private static void showSearchResults(List<BankAccount> foundAccounts) {
        if (foundAccounts.isEmpty()) {
            System.out.println("Accounts not found!");
        } else {
            System.out.println("Found accounts: " + foundAccounts.size());
            for (BankAccount account : foundAccounts) {
                System.out.println("---");
                account.information();
            }
        }
    }

    //Меню открытия нового счета
    private static void openAccountMenu() {
        System.out.println("\n--- Open Account ---");

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter BIC: ");
        String bic = scanner.nextLine();

        System.out.print("Enter name: ");
        String ownerName = scanner.nextLine();

        System.out.print("Enter INN: ");
        String inn = scanner.nextLine();

        System.out.print("Enter KPP: ");
        String kpp = scanner.nextLine();

        if (bankSystem.openAccount(accountNumber, bic, ownerName, inn, kpp)) {
            System.out.println("Account opened successfully!");
        } else {
            System.out.println("Error: account with this number already exists!");
        }
    }

    //Меню пополнения счета
    private static void topUpMenu() {
        System.out.println("\n--- Top-up ---");

        String accountNumber = getStringInput("Enter account number: ");
        float amount = getFloatInput("Enter top-up amount: ");

        if (bankSystem.topUp(accountNumber, amount)) {
            System.out.println("Successfully top-up for " + amount + " rub.");
        } else {
            System.out.println("Error: account not found or invalid amount!");
        }
    }

    //Меню снятия денег со счета
    private static void withdrawMenu() {
        System.out.println("\n--- Withdraw ---");

        String accountNumber = getStringInput("Enter account number: ");
        float amount = getFloatInput("Enter withdraw amount: ");

        if (bankSystem.withdraw(accountNumber, amount)) {
            System.out.println("Successfully withdrawn " + amount + " rub.");
        } else {
            System.out.println("Error: account not found, insufficient funds or invalid amount!");
        }
    }

    // Меню показа баланса
    private static void balanceMenu() {
        System.out.println("\n--- Account Balance ---");

        String accountNumber = getStringInput("Enter account number: ");
        Float balance = bankSystem.getBalance(accountNumber);

        if (balance != null) {
            System.out.println("Account balance: " + balance + " rub.");
        } else {
            System.out.println("Account not found!");
        }
    }

    //Меню показа истории операций
    private static void transactionsMenu() {
        System.out.println("\n--- Operation History ---");

        String accountNumber = getStringInput("Enter account number: ");
        List<String> transactions = bankSystem.getTransactions(accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("Account not found or no operations!");
        } else {
            System.out.println("Operation history for account " + accountNumber + ":");
            for (int i = 0; i < transactions.size(); i++) {
                System.out.println((i + 1) + ". " + transactions.get(i));
            }
        }
    }

    //Меню показа всех счетов
    private static void showAllAccountsMenu() {
        System.out.println("\n--- All Accounts ---");
        List<BankAccount> allAccounts = bankSystem.getAllAccounts();

        if (allAccounts.isEmpty()) {
            System.out.println("No accounts opened!");
        } else {
            System.out.println("Total accounts: " + allAccounts.size());
            for (BankAccount account : allAccounts) {
                System.out.println("---");
                account.information();
            }
        }
    }

    //Меню сохранения операций в файл
    private static void saveToFileMenu() {
        System.out.println("\n--- Save to File ---");

        String accountNumber = getStringInput("Account number: ");
        String filename = getStringInput("File name: ");

        if (bankSystem.saveToFile(accountNumber, filename)) {
            System.out.println("Saved to file: " + filename);
        } else {
            System.out.println("Error! Account not found.");
        }
    }

    //Меню конвертации валют
    private static void convertMoneyMenu() {
        System.out.println("\n--- Currency Converter ---");

        float money = getFloatInput("Amount: ");

        System.out.print("From (RUB/USD/EUR): ");
        String from = scanner.nextLine();

        System.out.print("To (RUB/USD/EUR): ");
        String to = scanner.nextLine();

        float result = bankSystem.convertMoney(money, from, to);

        System.out.println("Result: " + money + " " + from + " = " + result + " " + to);
    }

    //Меню показа курсов валют
    private static void showRatesMenu() {
        Converter.showRates();
    }


    // Вспомогательные методы для ввода данных
    private static String getStringInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error! Enter an integer.");
            }
        }
    }

    private static float getFloatInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error! Enter a number.");
            }
        }
    }
}