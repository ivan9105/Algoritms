package com.algoritms.dinner_philosopher;

import java.util.ArrayList;
import java.util.List;

public class DinnerPhilosopher {
    private static final int ITEMS_LENGTH = 5;

    public static void main(String[] args) {
        List<Fork> forks = new ArrayList<>();

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            forks.add(new Fork(String.format("Fork[%s]", i)));
        }

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            new Philosopher(String.format("Philosopher[%s]", i),
                    forks.get(i), forks.get((i + 1) % ITEMS_LENGTH)).start();
        }
    }
}
