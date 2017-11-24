package com.dht.process;

/**
 * 进程 Bean
 * @author dht925nerd@126.com
 */
public class Process {

    private int count;
    private int inTime;
    private int endTime;
    private boolean alive;

    public int getInTime() {
        return inTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getEndTime() {

        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Process(int count, int inTime) {
        this.count = count;
        this.inTime = inTime;
        this.alive = true;
    }
}
