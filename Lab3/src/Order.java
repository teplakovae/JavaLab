/**
 * Класс заказа в ресторане.
 * Содержит информацию о блюде, клиенте и статусе готовности.
 */
public class Order {
    private static int NEXT_ID = 1;

    private final int id;
    private final RestaurantConstants.Dish dish;
    private final String customerName;
    private final Client client;

    private boolean ready = false;

    public Order(RestaurantConstants.Dish dish,
                 String customerName,
                 Client client) {

        this.id = NEXT_ID++;
        this.dish = dish;
        this.customerName = customerName;
        this.client = client;
    }

    public int getId() { return id; }
    public RestaurantConstants.Dish getDish() { return dish; }
    public String getCustomerName() { return customerName; }
    public Client getClient() { return client; }

    public boolean isReady() { return ready; }
    public void setReady(boolean ready) { this.ready = ready; }
}



