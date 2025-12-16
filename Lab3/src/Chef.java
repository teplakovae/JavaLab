import java.util.List;
import java.util.concurrent.BlockingQueue;
/**
 * Класс повара на кухне.
 * Реализует интерфейс Runnable для выполнения в пуле потоков.
 * Каждый повар готовит заказы из очереди кухни.
 */
public class Chef implements Runnable {
    private final int id;
    private final BlockingQueue<Order> orderQueue;
    private final List<Order> readyOrders;

    public Chef(int id, BlockingQueue<Order> orderQueue, List<Order> readyOrders) {
        this.id = id;
        this.orderQueue = orderQueue;
        this.readyOrders = readyOrders;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Chef-" + id);
        System.out.println("Chef-" + id + ": Started work");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Берем заказ из очереди. Если очередь пустая, поток блокируется.
                Order order = orderQueue.take();
                prepareOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Chef-" + id + ": Finished work");
    }

    /**
     * Готовим заказ
     */
    private void prepareOrder(Order order) {
        System.out.println("Chef-" + id + ": Cooking " + order.getDish().getName()
                + " (order #" + order.getId() + ")");

        try {
            Thread.sleep(order.getDish().getCookingTime());
            order.markReady();

            synchronized (readyOrders) {
                readyOrders.add(order);
                readyOrders.notifyAll();// уведомляем все потоки, ожидающие готовности
            }

            System.out.println("Chef-" + id + ": Finished " + order.getDish().getName()
                    + " (order #" + order.getId() + ")");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            // Если повар прерван во время готовки, возвращаем заказ обратно в очередь
            try {
                orderQueue.put(order);
                System.out.println("Chef-" + id + ": Interrupted, returned order #"
                        + order.getId() + " to queue");
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
