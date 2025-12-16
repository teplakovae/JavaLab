import java.util.List;
import java.util.concurrent.*;
import java.util.ArrayList;
/**
 * Класс, управляющий кухней ресторана.
 * Создает и управляет пулом поваров (ExecutorService).
 */
public class Kitchen {
    private final BlockingQueue<Order> orderQueue;
    private final List<Order> readyOrders;
    private final ExecutorService chefPool;

    public Kitchen(int initialChefs,
                   BlockingQueue<Order> orderQueue,
                   List<Order> readyOrders) {

        this.orderQueue = orderQueue;
        this.readyOrders = readyOrders;
        this.chefPool = Executors.newFixedThreadPool(initialChefs);

        for (int i = 1; i <= initialChefs; i++) {
            Chef chef = new Chef(i, orderQueue, readyOrders);
            chefPool.submit(chef);
        }

        System.out.println("Kitchen: Started with " + initialChefs + " chefs");
    }

    public void shutdown() {
        chefPool.shutdownNow();
        try {
            if (!chefPool.awaitTermination(3, TimeUnit.SECONDS)) {
                System.out.println("Kitchen: Some chefs didn't finish in time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

