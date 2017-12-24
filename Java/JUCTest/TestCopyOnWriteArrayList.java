package com.dht.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList/CopyOnWriteArraySet : "写入并复制"
 * 注意：添加操作多时, 效率低, 因为每次添加时都会进行复制, 开销非常大
 *      并发迭代操作多时可以选择此类
 * @author dht925nerd@126.com
 */
public class TestCopyOnWriteArrayList {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();

        for (int i = 0; i < 10; i++){
            new Thread(myThread).start();
        }
    }

}

class MyThread implements Runnable{

//    private static List<String> list = new ArrayList<>();

    private static List<String> list = new CopyOnWriteArrayList<>();

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            list.add("NEW");
        }
    }
}
