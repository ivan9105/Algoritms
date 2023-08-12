package com.algoritms.knapsack;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class KnapsackProblem {

    private static final ThingsAggregation EMTPY_AGGR = new ThingsAggregation(emptyList(), 0);

    public static void main(String[] args) {
        var capacity = 4;

        var things = new Thing[]{
                new Thing(4, "Ожерелье", 4000),
                new Thing(1, "Кольцо", 2500),
                new Thing(3, "Подвеска", 2000)
        };

        var table = new ThingsAggregation[things.length][capacity];

        //вещи
        for (int i = 0; i < things.length; i++) {
            //веса
            for (int j = 1; j <= capacity; j++) {
                var thing = things[i];

                System.out.printf("Select thing: %s, compare with weight: %s%n", thing, j);

                // (j - 1) - индекс по весам
                if (j >= thing.weight) {
                    // предыдущий макс
                    var prevMax = i == 0 ? 0 : table[i - 1][j - 1].sum;
                    // стоимость текущей вещи + стоимость max оставшегося места

                    var currentPrice = 0;
                    var prevMaxThingForExtraWeight = i == 0 ? EMTPY_AGGR : table[i - 1][j - thing.weight];
                    if (i == 0) {
                        currentPrice = thing.price;
                    } else {
                        int extraCapacityThingWeight = prevMaxThingForExtraWeight.getWeight();
                        // если хватит места
                        if (j - thing.weight >= extraCapacityThingWeight) {
                            currentPrice = thing.price + prevMaxThingForExtraWeight.sum;
                        }
                    }


                    if (prevMax > currentPrice) {
                        table[i][j - 1] = table[i - 1][j - 1];
                    } else {
                        table[i][j - 1] = i == 0 ? new ThingsAggregation(List.of(thing), thing.price) : merge(thing, prevMaxThingForExtraWeight);

                    }
                } else {
                    // если вес меньше чем вес вещи то берем либо 0 стоимость (для первого ряда) либо предудущий максимум элементов
                    table[i][j - 1] = i == 0 ? EMTPY_AGGR : table[i - 1][j - 1];
                }
            }
        }

        var result = table[things.length - 1][capacity - 1];

        System.out.println("Result: " + result);
    }

    private static ThingsAggregation merge(Thing newThing, ThingsAggregation aggr) {
        var things = new ArrayList<>(aggr.getThings());
        things.add(newThing);

        return new ThingsAggregation(things, aggr.sum + newThing.price);
    }

    @Data
    @RequiredArgsConstructor
    private static class Thing {
        private final int weight;
        private final String name;
        private final int price;
    }

    @Data
    @RequiredArgsConstructor
    private static class ThingsAggregation {
        private final List<Thing> things;
        private final int sum;

        public int getWeight() {
            return things.stream().mapToInt(Thing::getWeight).sum();
        }
    }
}
