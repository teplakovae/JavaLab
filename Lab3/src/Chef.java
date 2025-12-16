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
    private volatile boolean running = true;

    public Chef(int id, BlockingQueue<Order> orderQueue, List<Order> readyOrders) {
        this.id = id;
        this.orderQueue = orderQueue;
        this.readyOrders = readyOrders;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Chef-" + id);
        System.out.println("Chef-" + id + ": Started work");

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Order order = orderQueue.take();
                prepareOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Chef-" + id + ": Finished work");
    }

    private void prepareOrder(Order order) {
        System.out.println("Chef-" + id + ": Cooking " + order.getDish().getName()
                + " (order #" + order.getId() + ")");

        try {
            Thread.sleep(order.getDish().getCookingTime());
            order.setReady(true);

            synchronized (readyOrders) {
                readyOrders.add(order);
                readyOrders.notifyAll();
            }

            System.out.println("Chef-" + id + ": Finished " + order.getDish().getName()
                    + " (order #" + order.getId() + ")");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (running) {
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

    public void stop() {
        running = false;
    }
}
