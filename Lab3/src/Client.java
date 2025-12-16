import java.util.concurrent.BlockingQueue;
/**
 * Класс клиента ресторана.
 * Каждый клиент работает в отдельном потоке.
 * Клиент заходит в ресторан, делает заказ, ждет блюдо, получает и уходит.
 */
public class Client extends Thread {
    private final BlockingQueue<ClientRequest> clientQueue;
    private RestaurantConstants.Dish chosenDish;

    private boolean served = false;

    public Client(int id, BlockingQueue<ClientRequest> clientQueue) {
        this.clientQueue = clientQueue;
        setName("Client-" + id);
    }

    @Override
    public void run() {
        System.out.println(getName() + ": Entered the restaurant");

        // Выбираем случайное блюдо из меню
        RestaurantConstants.Dish[] dishes = RestaurantConstants.Dish.values();
        chosenDish = dishes[(int)(Math.random() * dishes.length)];


        try {
            // Отправляем запрос официанту
            clientQueue.put(new ClientRequest(this, chosenDish));
            System.out.println(getName() + ": Waiting for waiter...");
        } catch (InterruptedException ignored) {}

        // Ждем, пока официант принесет блюдо
        synchronized (this) {
            while (!served) {
                try { wait(); }
                catch (InterruptedException ignored) {}
            }
        }

        System.out.println(getName() + ": Leaving the restaurant");
    }

    public synchronized void serveDish() {
        System.out.println(getName() + ": Received the dish!");
        served = true;
        notify();
    }

    // Рандомные чаевые
    public double getTip() {
        return Math.random() * 0.2 * chosenDish.getPrice();
    }
}
