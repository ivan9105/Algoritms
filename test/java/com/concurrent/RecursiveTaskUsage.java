package com.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.DoubleStream;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertNull;

/**
 * По факту удобно для распараллеливания рекурсивных задач с получением результата
 * Recursive action тоже самое только результат не возвращает
 */
public class RecursiveTaskUsage {

    private Random random = new Random();

    @Test
    public void summarize() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        double[] data = DoubleStream.generate(() -> random.nextDouble()).limit(1000000).toArray();
        Sum task = new Sum(data, 0, data.length);
        //таску еще не запустили
        Double currentResult = task.getRawResult();
        assertNull(currentResult);

        forkJoinPool.invoke(task);
    }

    class Sum extends RecursiveTask<Double> {
        private final Integer SEQUENCE_THRESHOLD = 500;
        private double[] data;
        private int start, end;

        public Sum(double[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            System.out.println(format("Current thread %s", Thread.currentThread().getId()));

            double sum = 0;
            if ((end - start) < SEQUENCE_THRESHOLD) {
                sum = stream(data, start, end).sum();
            } else {
                int middle = (start + end) >> 1; //делим на два
                Sum left = new Sum(data, start, middle);
                Sum right = new Sum(data, middle, end);

                //запускаем в асинхроне
                left.fork();
                right.fork();

                //ждем получения результат
                sum += left.join() + right.join();
            }
            return sum;
        }
    }
}
