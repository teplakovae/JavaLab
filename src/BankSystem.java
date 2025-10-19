import java.util.ArrayList;
import java.util.List;

/**
 * Класс BankSystem управляет всеми банковскими счетами
 * Основные банковские операции
 */
public class BankSystem {
    private List<BankAccount> accounts;

    public BankSystem(){
        accounts = new ArrayList<>();
    }

    //Находит счет по номеру
    private BankAccount findByNum(String accountNumber){
        for (BankAccount account : accounts){
            if (account.getAccountNumber().equals(accountNumber)){
                return account;
            }
        }
        return null;
    }

    //Список всех счетов
    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    //Открывает новый счет
    public boolean openAccount(String accountNumber, String BIC, String name, String INN, String KPP){
        if (findByNum(accountNumber) != null) {
            return false;
        }
        BankAccount newAcc = new BankAccount(accountNumber, BIC, name, INN, KPP);
        accounts.add(newAcc);
        return true;
    }

    //Пополняет счет
    public boolean topUp(String accountNumber, float amount){
        BankAccount account = findByNum(accountNumber);
        if (account == null || amount <= 0) {
            return false;
        }
        account.topUp(amount);
        return true;
    }

    //Снимает деньги со счета
    public boolean withdraw(String accountNumber, float amount) {
        BankAccount account = findByNum(accountNumber);
        if (account == null) {
            return false;
        }
        return account.withdraw(amount);
    }

    //Баланс счета
    public Float getBalance(String accountNumber){
        BankAccount account = findByNum(accountNumber);
        return account != null ? account.getBalance() : null;
    }

    //Получает историю операций по счету
    public List<String> getTransactions(String accountNumber){
        BankAccount account = findByNum(accountNumber);
        return account != null ? account.getOperations() : new ArrayList<>();
    }

    //Сохраняет операции счета в файл
    public boolean saveToFile(String accountNumber, String filename) {
        BankAccount account = findByNum(accountNumber);
        if (account == null) {
            return false;
        }
        return MakeFile.saveTransactionsToFile(account, filename);
    }

    //Конвертирует валюту
    public float convertMoney(float money, String from, String to) {
        return Converter.convert(money, from, to);
    }


    // Методы поиска счетов

    //Поиск по всем полям
    public List<BankAccount> searchAccounts(String searchText) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        String searchLower = searchText.toLowerCase();

        for (BankAccount account : accounts) {
            if (account.getAccountNumber().toLowerCase().contains(searchLower) ||
                    account.getBIC().toLowerCase().contains(searchLower) ||
                    account.getName().toLowerCase().contains(searchLower) ||
                    account.getINN().toLowerCase().contains(searchLower) ||
                    account.getKPP().toLowerCase().contains(searchLower)) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    //Поиск по номеру счета
    public List<BankAccount> searchByAccountNumber(String accountNumber) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().toLowerCase().contains(accountNumber.toLowerCase())) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    //Поиск по БИК
    public List<BankAccount> searchByBIC(String bic) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (account.getBIC().toLowerCase().contains(bic.toLowerCase())) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    //Поиск по имени владельца
    public List<BankAccount> searchByName(String ownerName) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (account.getName().toLowerCase().contains(ownerName.toLowerCase())) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    //Поиск по ИНН
    public List<BankAccount> searchByINN(String inn) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (account.getINN().toLowerCase().contains(inn.toLowerCase())) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    //Поиск по КПП
    public List<BankAccount> searchByKPP(String kpp) {
        List<BankAccount> foundAccounts = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (account.getKPP().toLowerCase().contains(kpp.toLowerCase())) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }
}