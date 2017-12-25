package com.dht.spring;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 1. 读锁可以允许多个读线程并发访问数据
 * 2. 写锁一次只允许一个写线程修改数据
 * @author dht925nerd@126.com
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        Note note = new Note();
        for (int i = 0; i < 10; i++){
            new Thread(new Writer(note), "写者-" + i).start();
            for (int j = 1; j <= 10; j++){
                new Thread(new Reader(note), "读者-" + (i*10+j)).start();
            }
        }
    }
}

class Note{
    private int number;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读操作
     */
    public void read(){
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在读取数据: " + number);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 写操作
     * @param number 新数据
     */
    public void write(int number){
        lock.writeLock().lock();
        try {
            this.number = number;
            System.out.println(Thread.currentThread().getName() + "正在修改数据... 新数据为: " + number);
        } finally {
            lock.writeLock().unlock();
        }
    }
}


class Reader implements Runnable{

    private Note note;

    public Reader(Note note) {
        this.note = note;
    }

    @Override
    public void run() {
        note.read();
    }
}

class Writer implements Runnable{

    private Note note;

    public Writer(Note note) {
        this.note = note;
    }

    @Override
    public void run() {
        note.write(new Random().nextInt(20));
    }
}
