package com.algoritms.knapsack;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class KnapsackProblem {

    private static final ThingsResult EMPTY = new ThingsResult(emptyList(), 0);

    public static void main(String[] args) {
        var capacity = 4;

        var things = new Thing[]{
                new Thing(4, "Ожерелье", 4000),
                new Thing(1, "Кольцо", 2500),
                new Thing(3, "Подвеска", 2000)
        };

        var table = new ThingsResult[things.length][capacity];

        //вещи
        for (int thingIndex = 0; thingIndex < things.length; thingIndex++) {
            //веса
            for (int weight = 1; weight <= capacity; weight++) {
                var thing = things[thingIndex];

                System.out.printf("Заполняем таблицу, вещь: %s[%d], вес: %s%n", thing.name, thingIndex, weight);

                // (weight - 1) - индекс по весам
                if (weight >= thing.weight) {
                    // предыдущий макс
                    var prevMaxPrice = thingIndex == 0 ? 0 : table[thingIndex - 1][weight - 1].sum;
                    // стоимость текущей вещи + стоимость max оставшегося места

                    var price = 0;
                    var prevMaxThingForExtraWeight = thingIndex == 0 ? EMPTY : table[thingIndex - 1][weight - thing.weight];
                    if (thingIndex == 0) {
                        price = thing.price;
                    } else {
                        int extraCapacityThingWeight = prevMaxThingForExtraWeight.getWeight();
                        // если хватит места
                        if (weight - thing.weight >= extraCapacityThingWeight) {
                            price = thing.price + prevMaxThingForExtraWeight.sum;
                        }
                    }


                    if (prevMaxPrice > price) {
                        table[thingIndex][weight - 1] = table[thingIndex - 1][weight - 1];
                    } else {
                        table[thingIndex][weight - 1] = thingIndex == 0
                                ? new ThingsResult(List.of(thing), thing.price)
                                : mergeResult(thing, prevMaxThingForExtraWeight);

                    }
                } else {
                    // если вес меньше чем вес вещи то берем либо 0 стоимость (для первого ряда) либо предудущий максимум элементов
                    table[thingIndex][weight - 1] = thingIndex == 0 ? EMPTY : table[thingIndex - 1][weight - 1];
                }
            }
        }

        var result = table[things.length - 1][capacity - 1];

        System.out.println("Результат: " + result);
    }

    private static ThingsResult mergeResult(Thing thing, ThingsResult prevResult) {
        var things = new ArrayList<>(prevResult.getThings());
        things.add(thing);

        return new ThingsResult(things, prevResult.sum + thing.price);
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
    private static class ThingsResult {
        private final List<Thing> things;
        private final int sum;

        public int getWeight() {
            return things.stream().mapToInt(Thing::getWeight).sum();
        }
    }
}
