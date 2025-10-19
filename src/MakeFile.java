import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс MakeFile для сохранения данных в файлы
 * Запись операций по счетам в текстовые файлы
 */
public class MakeFile {

    //Сохраняет все транзакции счета в файл
    public static boolean saveTransactionsToFile(BankAccount account, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            PrintWriter out = new PrintWriter(writer);

            // Записываем заголовок с информацией о счете
            out.println("=== Bank Account Transactions ===");
            out.println("Account: " + account.getAccountNumber());
            out.println("Owner: " + account.getName());
            out.println("BIC: " + account.getBIC());
            out.println("INN: " + account.getINN());
            out.println("KPP: " + account.getKPP());
            out.println("Current Balance: " + account.getBalance() + " RUB");

            // Получаем текущее время для записи в файл
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println("Generated: " + currentTime);
            out.println();
            out.println("=== Transaction History ===");

            // Записываем все операции по порядку
            int counter = 1;
            for (String transaction : account.getOperations()) {
                out.println(counter + ". " + transaction);
                counter++;
            }

            out.println();
            out.println("Total transactions: " + account.getOperations().size());

            out.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }
}