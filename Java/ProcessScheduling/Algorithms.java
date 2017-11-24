package com.dht.process;

import java.util.List;

/**
 * ProcessScheduling Algorithms
 *
 * @author dht925nerd@126.com
 */
public class Algorithms {

    /**
     * CPU 占用情况
     * 1: 空闲
     * 0: 正被占用
     */
    static int CPU = 1;

    /**
     * 先来先服务算法
     * @param processes
     */
    public static void FCFS(List<Process> processes){
        int count = processes.size();
        int time = 0;
        int[] waitQueue = new int[10];
        int front = 0;int tail = 0;
        int running = 0;
        int averageTime = 0;
        System.out.println("-------------FCFS算法-------------");
        if (count <= 0){
            System.out.println("无可用进程");
            return;
        }

        while (count > 0){
            System.out.print("第 " + time + " 秒: ");
            for (int i = 0; i < processes.size(); i++){
                if (processes.get(i).isAlive() && processes.get(i).getInTime() == time){
                    System.out.print("进程 " + i + " 到来 ");
                    waitQueue[tail] = i;
                    tail = (tail+1) % 10;
                }
                if (processes.get(running).isAlive() && processes.get(running).getCount() == 0){
                    System.out.print("进程 " + running + " 结束运行 ");
                    processes.get(running).setAlive(false);
                    processes.get(running).setEndTime(time);
                    count--;
                    CPU = 1;
                }
            }
            if (CPU == 1 && front != tail){
                running = waitQueue[front];
                front = (front+1) % 10;
                System.out.print("进程 " + running + " 开始运行");
                int temp = processes.get(running).getCount();
                temp--;
                processes.get(running).setCount(temp);
                CPU = 0;
            } else if (CPU == 0){
                int temp = processes.get(running).getCount();
                temp--;
                processes.get(running).setCount(temp);
            }
            time++;
            System.out.println();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------------------------");
        for (int i = 0; i < processes.size(); i++){
            int inTime = processes.get(i).getInTime();
            int endTime = processes.get(i).getEndTime();
            averageTime += endTime-inTime;
            System.out.println("进程 " + i + " : 到来时间: " +
                                inTime + " 结束时间: " +
                                endTime + " 周转时间: " +
                                (endTime-inTime));
        }
        System.out.println("平均周转时间: " + averageTime / processes.size());
        System.out.println("---------------END---------------");
    }

}
