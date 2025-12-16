import java.util.*;
import java.util.concurrent.*;

/**
 * Главный класс системы ресторана "Claude Monet".
 * Запускает и управляет всей симуляцией работы ресторана.
 */

public class RestaurantMain {
    public static double totalIncome = 0;
    public static void main(String[] args) {
        System.out.println("Restaurant Claude Monet Opened\n");
        BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
        BlockingQueue<ClientRequest> clientQueue = new LinkedBlockingQueue<>();
        List<Order> readyOrders = Collections.synchronizedList(new ArrayList<>());

        // Создание и запуск официантов
        List<Waiter> waiters = new ArrayList<>();
        for (int i = 1; i <= RestaurantConstants.WAITERS_COUNT; i++) {
            Waiter waiter = new Waiter(i, kitchenQueue, readyOrders, clientQueue);
            waiters.add(waiter);
            waiter.start();
        }

        // Создание кухни с пулом поваров
        Kitchen kitchen = new Kitchen(
                RestaurantConstants.INITIAL_CHEFS_COUNT,
                kitchenQueue,
                readyOrders
        );

        // Создание и запуск клиентов с задержкой
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= RestaurantConstants.MAX_CUSTOMERS; i++) {
            Client client = new Client(i, clientQueue);
            clients.add(client);

            try { Thread.sleep((long)(Math.random() * RestaurantConstants.MAX_CLIENT_DELAY_MS)); }
            catch (InterruptedException ignored) {}

            client.start();
        }

        // Ожидание всех клиентов
        for (Client c : clients) {
            try { c.join(); }
            catch (InterruptedException ignored) {}
        }

        System.out.println("All clients have been served.");

        // Ждем, пока кухня и официанты закончат обработку заказов
        while (!kitchenQueue.isEmpty() || !readyOrders.isEmpty()) {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        // Завершаем работу официантов
        for (Waiter waiter : waiters) waiter.interrupt();
        for (Waiter waiter : waiters) {
            try { waiter.join(); } catch (InterruptedException ignored) {}
        }

        // Завершаем работу кухни
        kitchen.shutdown();

        System.out.printf("\nRestaurant Claude Monet Closed. Total income: $%.2f\n", totalIncome);

    }
}

//На основе сериала "Кухня" :)
