/**
 * Класс Converter для конвертации валют
 * Использует фиксированные курсы валют
 */
public class Converter {
    // Курсы валют (фиксированные)
    private static final float USD_TO_RUB = 90.0f;
    private static final float EUR_TO_RUB = 100.0f;

    //Конвертирует сумму из одной валюты в другую
    public static float convert(float money, String from, String to) {
        // Если валюта одинаковая - ничего не меняем
        if (from.equals(to)) {
            return money;
        }

        // Конвертируем в рубли
        float inRubles = money;
        if (from.equals("USD")) {
            inRubles = money * USD_TO_RUB;
        } else if (from.equals("EUR")) {
            inRubles = money * EUR_TO_RUB;
        }

        // Конвертируем из рублей в нужную валюту
        float result = inRubles;
        if (to.equals("USD")) {
            result = inRubles / USD_TO_RUB;
        } else if (to.equals("EUR")) {
            result = inRubles / EUR_TO_RUB;
        }

        return result;
    }

    //Показывает текущие курсы валют
    public static void showRates() {
        System.out.println("=== Exchange Rates ===");
        System.out.println("1 USD = " + USD_TO_RUB + " RUB");
        System.out.println("1 EUR = " + EUR_TO_RUB + " RUB");
        System.out.println("1 RUB = " + (1/USD_TO_RUB) + " USD");
        System.out.println("1 RUB = " + (1/EUR_TO_RUB) + " EUR");
    }
}
