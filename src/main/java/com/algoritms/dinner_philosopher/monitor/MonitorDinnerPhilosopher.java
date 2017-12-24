package com.algoritms.dinner_philosopher.monitor;

import java.util.ArrayList;
import java.util.List;

public class MonitorDinnerPhilosopher {
    private static final int ITEMS_LENGTH = 5;

    public static void main(String[] args) {
        List<MonitorFork> forks = new ArrayList<>();
        for (int i = 0; i < ITEMS_LENGTH; i++) {
            forks.add(new MonitorFork(String.format("Fork[%s]", i)));
        }

        Monitor monitor = new Monitor(ITEMS_LENGTH);

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            new Thread(new MonitorPhilosopher(i, forks.get(i), forks.get((i + 1) % ITEMS_LENGTH), monitor)).start();
        }
    }
}
