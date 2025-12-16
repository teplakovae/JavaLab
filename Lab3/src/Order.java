import java.util.concurrent.atomic.AtomicInteger;
/**
 * Класс заказа в ресторане.
 * Содержит информацию о блюде, клиенте и статусе готовности.
 */

public class Order {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final RestaurantConstants.Dish dish;
    private final String customerName;
    private final Client client;

    private volatile boolean ready = false;

    public Order(RestaurantConstants.Dish dish,
                 String customerName,
                 Client client) {

        this.id = NEXT_ID.getAndIncrement();
        this.dish = dish;
        this.customerName = customerName;
        this.client = client;
    }

    public RestaurantConstants.Dish getDish() {
        return dish;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Client getClient() {
        return client;
    }

    public int getId() {
        return id;
    }


    public boolean isReady() {
        return ready;
    }

    public void markReady() {
        this.ready = true;
    }
}
