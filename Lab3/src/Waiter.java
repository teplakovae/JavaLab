import java.util.List;
import java.util.concurrent.BlockingQueue;
/**
 * Класс официанта ресторана.
 * Каждый официант работает в отдельном потоке.
 * Официант принимает заказы от клиентов, передает их на кухню и доставляет готовые блюда клиентам.
 */
public class Waiter extends Thread {

    private final BlockingQueue<Order> kitchenQueue;
    private final List<Order> readyOrders;
    private final BlockingQueue<ClientRequest> clientQueue;

    public Waiter(int id,
                  BlockingQueue<Order> kitchenQueue,
                  List<Order> readyOrders,
                  BlockingQueue<ClientRequest> clientQueue) {

        this.kitchenQueue = kitchenQueue;
        this.readyOrders = readyOrders;
        this.clientQueue = clientQueue;

        setName("Waiter-" + id);
    }

    @Override
    public void run() {
        System.out.println(getName() + ": Started working");

        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Берем новый запрос клиента
                ClientRequest req = clientQueue.take();

                System.out.println(getName() + ": Taking order from "
                        + req.getClient().getName() + " for " + req.getDish().getName());
                // Создаем заказ для кухни
                Order order = new Order(req.getDish(), req.getClient().getName(), req.getClient());
                kitchenQueue.put(order);

                waitForOrder(order);
                deliverOrder(order);
            }

        } catch (InterruptedException ignored) {
        }

        System.out.println(getName() + ": Going home");
    }


    private void waitForOrder(Order order) throws InterruptedException {
        synchronized (readyOrders) {
            while (!readyOrders.contains(order)) {
                readyOrders.wait();
            }
            readyOrders.remove(order);
        }

    }

    private void deliverOrder(Order order) {
        System.out.println(getName() + ": Delivering "
                + order.getDish().getName() + " to " + order.getCustomerName());
        double tip = order.getClient().getTip();
        double income = order.getDish().getPrice() + tip;

        // Синхронизируем доступ к общему доходу
        synchronized (RestaurantMain.class) {
            RestaurantMain.totalIncome += income;
        }
        System.out.printf("%s: Delivering %s to %s | Dish: $%.2f, Tip: $%.2f, Total so far: $%.2f\n",
                getName(),
                order.getDish().getName(),
                order.getCustomerName(),
                order.getDish().getPrice(),
                tip,
                RestaurantMain.totalIncome);

        // Сообщаем клиенту, что заказ доставлен
        order.getClient().serveDish();
    }
}
