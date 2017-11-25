import java.util.List;

/**
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
     * 等待队列长度
     */
    static final int MAXLEN = 10;

    /**
     * 先来先服务算法
     * @param processes
     */
    public static void FCFS(List<Process> processes){
        int count = processes.size();
        int time = 0;
        int[] waitQueue = new int[MAXLEN];
        int front = 0;int tail = 0;
        int running = 0;
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
                    tail = (tail+1) % MAXLEN;
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
                front = (front+1) % MAXLEN;
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
        }
        System.out.println("---------------------------------");
        ShowResult(processes);
    }

    /**
     * 短作业优先算法
     * @param processes
     */
    public static void SJF(List<Process> processes){
        int count = processes.size();
        int time = 0;
        int[] waitQueue = new int[MAXLEN];
        int length = 0;
        int front = 0;int tail = 0;
        int running = 0;
        System.out.println("-------------SJF算法-------------");
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
                    tail = (tail+1) % MAXLEN;
                    length++;
                    /**
                     * 对等待队列按进程运行时长按从小到大排序
                     */
                    for (int x=front, z=0; z < length; x=(x+1)%MAXLEN, z++){
                        for (int y=x+1, q=0; q < length-x; y=(y+1)%MAXLEN, q++){
                            if (processes.get(waitQueue[x]).getCount() > processes.get(waitQueue[y]).getCount()){
                                int t = waitQueue[x];
                                waitQueue[x] = waitQueue[y];
                                waitQueue[y] = t;
                            }
                        }
                    }
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
                front = (front+1) % MAXLEN;
                length--;
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
        }
        System.out.println("--------------------------------");
        ShowResult(processes);
    }

    /**
     * 优先级调度算法
     * @param processes
     */
    public static void PSA(List<Process> processes){
        int count = processes.size();
        int time = 0;
        int[] waitQueue = new int[MAXLEN];
        int length = 0;
        int front = 0;int tail = 0;
        int running = 0;
        System.out.println("-------------PSA算法-------------");
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
                    tail = (tail+1) % MAXLEN;
                    length++;
                    /**
                     * 对等待队列按进程优先级按从小到大排序
                     */
                    for (int x=front, z=0; z < length; x=(x+1)%MAXLEN, z++){
                        for (int y=x+1, q=0; q < length-x; y=(y+1)%MAXLEN, q++){
                            if (processes.get(waitQueue[x]).getPriority() > processes.get(waitQueue[y]).getPriority()){
                                int t = waitQueue[x];
                                waitQueue[x] = waitQueue[y];
                                waitQueue[y] = t;
                            }
                        }
                    }
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
                front = (front+1) % MAXLEN;
                length--;
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
        }
        System.out.println("--------------------------------");
        ShowResult(processes);
    }

    /**
     * 时间片轮转算法
     * @param processes
     * @param round
     */
    public static void RR(List<Process> processes, int round){
        int count = processes.size();
        int time = 0;
        int[] waitQueue = new int[MAXLEN];
        int front = 0;int tail = 0;
        int running = 0;
        System.out.println("------------- RR 算法-------------");
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
                    tail = (tail+1) % MAXLEN;
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
                front = (front+1) % MAXLEN;
                System.out.print("进程 " + running + " 开始运行");
                int temp = processes.get(running).getCount();
                temp--;
                processes.get(running).setCount(temp);
                CPU = 0;
            } else if (CPU == 0){
                int temp = processes.get(running).getCount();
                temp--;
                processes.get(running).setCount(temp);
                if (time % round == 0){
                    System.out.print("进程 " + running + " 暂停运行");
                    waitQueue[tail] = running;
                    tail = (tail+1) % MAXLEN;
                    CPU = 1;
                }
            }
            time++;
            System.out.println();
        }
        System.out.println("---------------------------------");
        ShowResult(processes);
    }

    /**
     * 输出时间统计结果
     * @param processes
     */
    public static void ShowResult(List<Process> processes){
        int averageTime = 0;
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
        System.out.println("---------------END--------------");
    }

}
