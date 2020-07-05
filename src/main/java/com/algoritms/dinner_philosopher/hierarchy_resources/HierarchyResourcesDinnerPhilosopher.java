package com.algoritms.dinner_philosopher.hierarchy_resources;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * последней вилке проставляем обратный порядок при захвате ресурсов
 * за счет этого проблема с deadlock решается
 */
public class HierarchyResourcesDinnerPhilosopher {
    private static final int ITEMS_LENGTH = 5;

    public static void main(String[] args) {
        List<HierarchyResourcesFork> forks = new ArrayList<>();

        for (int order = 0; order < ITEMS_LENGTH; order++) {
            String id = format("Fork[%s]", order);
            forks.add(new HierarchyResourcesFork(id, order));
        }

        for (int firstForkIndex = 0; firstForkIndex < ITEMS_LENGTH; firstForkIndex++) {
            int secondForkIndex = (firstForkIndex + 1) % ITEMS_LENGTH;

            String philosopherName = format("Philosopher[%s]", firstForkIndex);
            HierarchyResourcesFork leftFork = forks.get(firstForkIndex);
            HierarchyResourcesFork rightFork = forks.get(secondForkIndex);

            new HierarchyResourcesPhilosopher(philosopherName, leftFork, rightFork).start();
        }
    }
}
