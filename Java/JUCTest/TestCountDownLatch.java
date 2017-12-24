package com.dht.spring;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch : 闭锁, 在完成某些运算时, 只有其他的线程都完成后, 当前线程才能运行
 * @author dht925nerd@126.com
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
        //初始化闭锁的个数, 每个线程完成后将数值减一, 当减为 0 时, 当前线程就可以执行
        final CountDownLatch latch = new CountDownLatch(5);
        LatchDemo latchDemo = new LatchDemo(latch);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++){
            new Thread(latchDemo).start();
        }
        long end = System.currentTimeMillis();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("执行时间: " + (end - start));
    }

}

class LatchDemo implements Runnable{

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        synchronized (this){
            try{
                for (int i = 0; i < 50000; i++){
                    if (i % 2 == 0){
                        System.out.println(i);
                    }
                }
            } finally {
                //必须保证线程完成后执行减一操作
                latch.countDown();
            }
        }
    }
}
