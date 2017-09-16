
/**
 * there is an integer array(length:5),
 * I create four threads and named with a number
 * they will increased the number corresponding to the array index
 * and all these threads execute concurrently
 * @author 党昊天 <dht925nerd@126.com>
 * @version : 2017/9/9.
 */
public class Thread_NumberGrowth {

    int[] array = new int[5];

    public void show(int[] a){
        for(int i = 0; i < a.length; i++){
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    public void test() {
        Runnable r = () -> {
            try {
                Integer name = Integer.parseInt(Thread.currentThread().getName());
                System.out.println("Thread " + name + " start!!!");
                for (int i = 0 ; i < 10; i++) {
                    array[name]++;
                    Thread.sleep(name*100);
                    System.out.print("Thread " + name + ":");
                    show(array);
                }
                System.out.println("Thread " + name + " is done!!!");
            } catch (InterruptedException e){}
        };

        Thread t0 = new Thread(r, "0");
        Thread t1 = new Thread(r, "1");
        Thread t2 = new Thread(r, "2");
        Thread t3 = new Thread(r, "3");

        t0.start();
        t1.start();
        t2.start();
        t3.start();
    }

    public static void main(String... avgs) {
        Test t = new Test();
        t.test();
    }
}
