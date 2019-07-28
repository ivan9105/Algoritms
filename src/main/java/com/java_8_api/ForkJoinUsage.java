package com.java_8_api;

import java.util.concurrent.RecursiveAction;

import static java.lang.String.format;

public class ForkJoinUsage {
    public static void main(String[] args) {
        ForkJoinUsage executor = new ForkJoinUsage();
        executor.execute();
    }

    private void execute() {
        long[] input = new long[10_000];
        for (int i = 0; i < input.length; i++) {
            input[i] = Math.round(Math.random() * 1000);
        }
        SortAction sortAction = new SortAction(input);
        sortAction.compute();
    }
}

class SortAction extends RecursiveAction {
    private final int THRESHOLD = 1000;
    private long[] input;
    private int start;
    private int end;

    public SortAction(long[] input) {
        this.input = input;
        this.start = 0;
        this.end = input.length - 1;
    }

    public SortAction(long[] input, int start, int end) {
        this.input = input;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        int size = end - start;
        if (size > THRESHOLD) {
            split(size);
        }
        quickSort(input, start, end);
    }

    /**
     * https://ru.wikipedia.org/wiki/%D0%91%D1%8B%D1%81%D1%82%D1%80%D0%B0%D1%8F_%D1%81%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0
     */
    private void quickSort(long[] input, int start, int end) {
        if (start >= end) {
            return;
        }

        int splitIndex = partition(input, start, end);
        quickSort(input, start, splitIndex - 1);
        quickSort(input, splitIndex + 1, end);
    }

    private int partition(long[] input, int start, int end) {
        long pivotValue = input[end];
        int pivotIndex = start;

        for (int i = start; i < end; i++) {
            if (input[i] < pivotValue) {
                swap(input, i, pivotIndex);
                pivotIndex++;
            }
        }
        swap(input, pivotIndex, end);
        return pivotIndex;
    }

    private void swap(long[] input, int x, int y) {
        long temp = input[x];
        input[x] = input[y];
        input[y] = temp;
    }


    private void split(int size) {
        System.out.println(format("Запуск параллельной обработки. Start: %s, End: %s", start, end));
        SortAction leftAction = new SortAction(input, start, start + size / 2);
        leftAction.fork(); //left запускаем в отдельном потоке
        SortAction rightAction = new SortAction(input, start + size / 2, end);
        rightAction.compute(); //right считаем в текущем потоке
        leftAction.join(); //ждем пока выполнение left action не заночиться
        System.out.println(format("Параллельная обработка окончена. Start: %s, End: %s", start, end));
    }
}