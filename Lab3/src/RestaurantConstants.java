/**
 * Класс констант для ресторана.
 * Содержит основные параметры и меню.
 */
public class RestaurantConstants {
    public static final int MAX_CUSTOMERS = 20;
    public static final int WAITERS_COUNT = 4;
    public static final int INITIAL_CHEFS_COUNT = 4;
    public static final int MAX_CLIENT_DELAY_MS = 3000;

    public enum Dish {
        FRIED_DORADO_IN_SPICES(5000, "Fried dorado in spices", 25.0),
        FILET_MIGNON_WITH_SPICES(6000, "Filet mignon with mushrooms", 30.0),
        RATATOUILLE(4000, "Ratatouille", 15.0),
        DUCK_LEGS_CONFIT(5000, "Duck legs confit", 28.0),
        WARM_SEAFOOD_SALAD(3000, "Warm seafood salad", 20.0),
        FRITATOTATOTATO(7000, "Fritatotatotato", 18.0),
        CROISSANTS(2000, "Croissants", 5.0);


        private final int cookingTime;
        private final String name;
        private final double price;

        Dish(int time, String name, double price) {
            this.cookingTime = time;
            this.name = name;
            this.price = price;
        }

        public int getCookingTime() { return cookingTime; }
        public String getName() { return name; }
        public double getPrice() { return price; }

    }
}
