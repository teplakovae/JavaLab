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

                ClientRequest req = clientQueue.take();

                System.out.println(getName() + ": Taking order from "
                        + req.client.getName() + " for " + req.dish.getName());

                Order order = new Order(req.dish, req.client.getName(), req.client);
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
            while (!order.isReady()) {
                readyOrders.wait();
            }
            readyOrders.remove(order);
        }
    }

    private void deliverOrder(Order order) {
        System.out.println(getName() + ": Delivering "
                + order.getDish().getName() + " to " + order.getCustomerName());

        order.getClient().serveDish();
    }
}

