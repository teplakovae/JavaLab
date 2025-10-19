import java.util.ArrayList;
import java.util.List;

/**
 * Класс BankAccount представляет банковский счет
 * Содержит информацию о счете и операции с ним
 */
public class BankAccount {
    private String accountNum;
    private String BIC;
    private String name;
    private String INN;
    private String KPP;
    private float balance;
    private List<String> operations;


    //Создает новый банковский счет
    public BankAccount(String accountNum, String BIC, String name, String INN, String KPP){
        this.accountNum = accountNum;
        this.BIC = BIC;
        this.name = name;
        this.INN = INN;
        this.KPP = KPP;
        this.balance = 0;
        this.operations = new ArrayList<>();
        this.operations.add("The bank account opened!");
    }

    // Геттеры для получения информации о счете
    public String getAccountNumber(){ return accountNum; }
    public String getBIC(){ return BIC; }
    public String getName(){ return name; }
    public String getINN(){ return INN; }
    public String getKPP(){ return KPP; }
    public float getBalance(){ return balance; }
    public List<String> getOperations(){ return operations; }

    public void information(){
        System.out.println("Account Number: " + accountNum);
        System.out.println("Owner Name: " + name);
        System.out.println("BIC: " + BIC);
        System.out.println("INN: " + INN);
        System.out.println("KPP: " + KPP);
        System.out.println("Balance: " + balance);
    }

   //Пополнение счета
    public void topUp(float amount){
        if (amount > 0){
            balance += amount;
            operations.add("Top-up for " + amount + " rubles");
        }
        else{
            operations.add("Top-up for " + amount + " rubles is declined");
        }
    }

    //Снятие денег
    public boolean withdraw(float amount){
        if (amount > 0 && amount <= balance){
            balance -= amount;
            operations.add("Withdrawal of " + amount + " rubles");
            return true;
        }
        else{
            operations.add("Withdrawal of " + amount + " rubles is declined");
            return false;
        }
    }
}
