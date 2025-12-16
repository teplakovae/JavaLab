import java.util.*;
import java.util.concurrent.*;

/**
 * Главный класс системы ресторана "Claude Monet".
 * Запускает и управляет всей симуляцией работы ресторана.
 */

public class RestaurantMain {
    public static void main(String[] args) {
        System.out.println("Restaurant Claude Monet Opened\n");

        BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
        BlockingQueue<ClientRequest> clientQueue = new LinkedBlockingQueue<>();
        List<Order> readyOrders = Collections.synchronizedList(new ArrayList<>());

        List<Waiter> waiters = new ArrayList<>();
        for (int i = 1; i <= RestaurantConstants.WAITERS_COUNT; i++) {
            Waiter waiter = new Waiter(i, kitchenQueue, readyOrders, clientQueue);
            waiters.add(waiter);
            waiter.start();
        }

        Kitchen kitchen = new Kitchen(
                RestaurantConstants.INITIAL_CHEFS_COUNT,
                kitchenQueue,
                readyOrders
        );

        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= RestaurantConstants.MAX_CUSTOMERS; i++) {
            Client client = new Client(i, clientQueue);
            clients.add(client);

            try { Thread.sleep((long)(Math.random() * RestaurantConstants.MAX_CLIENT_DELAY_MS)); }
            catch (InterruptedException ignored) {}

            client.start();
        }

        for (Client c : clients) {
            try { c.join(); }
            catch (InterruptedException ignored) {}
        }

        System.out.println("All clients have been served.");

        while (!kitchenQueue.isEmpty() || !readyOrders.isEmpty()) {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        for (Waiter waiter : waiters) waiter.interrupt();
        for (Waiter waiter : waiters) {
            try { waiter.join(); } catch (InterruptedException ignored) {}
        }

        kitchen.shutdown();

        System.out.println("\nRestaurant Claude Monet Closed");
    }
}

//На основе сериала "Кухня" :)

