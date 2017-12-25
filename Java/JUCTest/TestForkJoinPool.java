package com.dht.spring;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author dht925nerd@126.com
 */
public class TestForkJoinPool {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        ForkJoinTask<Long> task = new ForkJoinSumCalculate(0L, 10000000L);

        Long sum = pool.invoke(task);

        System.out.println(sum);
    }


    /**
     * java8 新特性, CPU 利用率可以达到 100%
     */
    @Test
    public void test(){
        Instant start = Instant.now();

        Long sum = LongStream.rangeClosed(0L, 50000000L).parallel().reduce(0L, Long::sum);

        System.out.println(sum);

        Instant end = Instant.now();

        System.out.println("耗时: " + Duration.between(start, end).toMillis());
    }

}


class ForkJoinSumCalculate extends RecursiveTask<Long>{

    private long start;
    private long end;

    private static final long THURSHOLD = 100L;  //临界值

    public ForkJoinSumCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;

        if (length <= THURSHOLD){
            long sum = 0L;

            for (long i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        } else {
            long middle = (start + end) / 2;
            ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle);
            left.fork();

            ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle + 1, end);
            right.fork();
            return left.join() + right.join();
        }
    }
}
