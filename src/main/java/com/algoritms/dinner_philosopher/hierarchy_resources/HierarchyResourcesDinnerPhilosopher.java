package com.algoritms.dinner_philosopher.hierarchy_resources;

import java.util.ArrayList;
import java.util.List;

public class HierarchyResourcesDinnerPhilosopher {
    private static final int ITEMS_LENGTH = 5;

    public static void main(String[] args) {
        List<HierarchyResourcesFork> forks = new ArrayList<>();

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            forks.add(new HierarchyResourcesFork(String.format("Fork[%s]", i), i));
        }

        for (int i = 0; i < ITEMS_LENGTH; i++) {
            new HierarchyResourcesPhilosopher(String.format("Philosopher[%s]", i),
                    forks.get(i), forks.get((i + 1) % ITEMS_LENGTH)).start();
        }
    }
}
