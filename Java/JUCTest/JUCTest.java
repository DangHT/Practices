package com.dht.spring;

/**
 * @author dht925nerd@126.com
 */
public class JUCTest {

    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();
        for (;;){
            if (td.isFlag()) {
                System.out.println("---------");
                break;
            }
        }
    }

}

class ThreadDemo implements Runnable{

    private volatile boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);

            flag = true;

            System.out.println("flag = " + isFlag());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
