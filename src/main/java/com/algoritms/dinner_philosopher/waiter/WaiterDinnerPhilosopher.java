package com.algoritms.dinner_philosopher.waiter;

import com.algoritms.dinner_philosopher.Fork;

import java.util.ArrayList;
import java.util.List;

public class WaiterDinnerPhilosopher {
    private static final int ITEMS_LENGTH = 5;

    public static void main(String[] args) {
        List<Fork> forks = new ArrayList<>();

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            forks.add(new Fork(String.format("Fork[%s]", i)));
        }

        List<WaiterPhilosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < ITEMS_LENGTH; i++) {
            philosophers.add(new WaiterPhilosopher(String.format("Philosopher[%s]", i),
                    forks.get(i), forks.get((i + 1) % ITEMS_LENGTH)));
        }

        new Waiter(philosophers);
        philosophers.forEach(Thread::start);
    }
}
