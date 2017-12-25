package com.dht.spring;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程交替运行
 * @author dht925nerd@126.com
 */
public class ThreadAlternate {
    public static void main(String[] args) {
        AlternateDemo demo = new AlternateDemo();

        new Thread(new ThreadA(demo), "A").start();
        new Thread(new ThreadB(demo), "B").start();
        new Thread(new ThreadC(demo), "C").start();
    }
}

class AlternateDemo {
    private int flag = 1;

    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void loopA(int loopTime){
        lock.lock();
        try {
            if (flag != 1){
                conditionA.await();
            }
            for (int i = 1; i <= 1; i++){
                System.out.println(Thread.currentThread().getName() + "\t次数:" + i + "\t回合数:" + loopTime);
            }
            //结束后唤醒 B 线程
            flag = 2;
            conditionB.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int loopTime){
        lock.lock();
        try {
            if (flag != 2){
                conditionB.await();
            }
            for (int i = 1; i <= 2; i++){
                System.out.println(Thread.currentThread().getName() + "\t次数:" + i + "\t回合数:" + loopTime);
            }
            //结束后唤醒 C 线程
            flag = 3;
            conditionC.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int loopTime){
        lock.lock();
        try {
            if (flag != 3){
                conditionC.await();
            }
            for (int i = 1; i <= 3; i++){
                System.out.println(Thread.currentThread().getName() + "\t次数:" + i + "\t回合数:" + loopTime);
            }
            //结束后唤醒 A 线程
            flag = 1;
            conditionA.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class ThreadA implements Runnable{

    private AlternateDemo alternateDemo;

    public ThreadA(AlternateDemo alternateDemo) {
        this.alternateDemo = alternateDemo;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            alternateDemo.loopA(i);
        }
    }
}

class ThreadB implements Runnable{

    private AlternateDemo alternateDemo;

    public ThreadB(AlternateDemo alternateDemo) {
        this.alternateDemo = alternateDemo;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            alternateDemo.loopB(i);
        }
    }
}

class ThreadC implements Runnable{

    private AlternateDemo alternateDemo;

    public ThreadC(AlternateDemo alternateDemo) {
        this.alternateDemo = alternateDemo;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            alternateDemo.loopC(i);
            System.out.println("----------------------");
        }
    }
}