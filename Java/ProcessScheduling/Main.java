package com.dht.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author dht925nerd@126.com
 */
public class Main {

    public static void main(String[] args) {
        int num;
        List<Process> processes = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        System.out.print("请输入共有多少进程：");
        num = in.nextInt();
        System.out.println("请输入每个进程的(运行时长 到来时间)：");
        for (int i = 0; i < num; i++){
            System.out.print("进程 " + i + " : ");
            int runTime = in.nextInt();
            int inTime = in.nextInt();
            Process p = new Process(runTime, inTime);
            processes.add(p);
        }
        Algorithms.SJF(processes);
    }

}
