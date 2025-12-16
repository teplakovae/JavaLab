/**
 * Класс для передачи заказа от клиента официанту.
 */
public class ClientRequest {
    public final Client client;
    public final RestaurantConstants.Dish dish;

    public ClientRequest(Client client, RestaurantConstants.Dish dish) {
        this.client = client;
        this.dish = dish;
    }
}


