/**
 * Класс констант для ресторана.
 * Содержит основные параметры и меню.
 */
public class RestaurantConstants {
    public static final int MAX_CUSTOMERS = 20;
    public static final int WAITERS_COUNT = 5;
    public static final int INITIAL_CHEFS_COUNT = 6;
    public static final int MAX_CLIENT_DELAY_MS = 3000;

    public enum Dish {
        FRIED_DORADO_IN_SPICES(5000, "Fried dorado in spices"),
        FILET_MIGNON_WITH_SPICES(6000, "Filet mignon with mushrooms"),
        RATATOUILLE(4000, "Ratatouille"),
        DUCK_LEGS_CONFIT(5000, "Duck legs confit"),
        WARM_SEAFOOD_SALAD(3000, "Warm seafood salad"),
        FRITATOTATOTATO(7000, "Fritatotatotato"),
        CROISSANTS(2000, "Croissants");

        private final int cookingTime;
        private final String name;

        Dish(int time, String name) {
            this.cookingTime = time;
            this.name = name;
        }

        public int getCookingTime() { return cookingTime; }
        public String getName() { return name; }
    }
}

