import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者与消费者问题实现
 * @author dht925nerd@126.com
 */
public class ProducerAndCustomer {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Customer customer = new Customer(clerk);
        Producer producer = new Producer(clerk);

        new Thread(producer, "生产者A").start();
        new Thread(customer, "消费者A").start();

        new Thread(customer, "消费者B").start();
        new Thread(producer, "生产者B").start();

        new Thread(customer, "消费者C").start();
        new Thread(customer, "消费者D").start();
        new Thread(producer, "生产者C").start();
        new Thread(producer, "生产者D").start();
        new Thread(producer, "生产者E").start();
        new Thread(producer, "生产者F").start();

        new Thread(customer, "消费者E").start();
        new Thread(customer, "消费者F").start();

    }

}

class Clerk{

    private int production = 0;
    private int max = 2;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Clerk(){}

    public Clerk(int max) {
        this.max = max;
    }

    /**
     * 向消费者售出商品
     */
    public void sale(){

        lock.lock();

        try{
            while (production <= 0){
                System.out.println(Thread.currentThread().getName() + "库存不足!");
                try{
                    condition.await();
                }catch (InterruptedException e){

                }
            }
            System.out.println(Thread.currentThread().getName() + ": 买走了一件商品");
            production--;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 生产者放入商品
     */
    public void put() {
        lock.lock();

        try {
            while (production >= max) {
                System.out.println(Thread.currentThread().getName() + "库存已满!");
                try {
                    condition.await();
                } catch (InterruptedException e) {

                }
            }
            System.out.println(Thread.currentThread().getName() + ": 生产了一件商品");
            production++;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class Producer implements Runnable{

    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        clerk.put();
    }
}

class Customer implements Runnable{

    private Clerk clerk;

    public Customer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        clerk.sale();
    }
}
