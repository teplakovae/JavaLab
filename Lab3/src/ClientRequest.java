/**
 * Класс для передачи заказа от клиента официанту.
 */
public class ClientRequest {
    private final Client client;
    private final RestaurantConstants.Dish dish;

    public Client getClient() {
        return client;
    }

    public RestaurantConstants.Dish getDish() {
        return dish;
    }


    public ClientRequest(Client client, RestaurantConstants.Dish dish) {
        this.client = client;
        this.dish = dish;
    }
}
