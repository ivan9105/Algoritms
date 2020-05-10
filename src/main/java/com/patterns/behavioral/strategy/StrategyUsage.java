package com.patterns.behavioral.strategy;

import lombok.AllArgsConstructor;

import java.util.List;

import static java.util.Arrays.asList;

public class StrategyUsage {
    /**
     * это поведенческий паттерн проектирования,
     * который определяет семейство схожих алгоритмов и помещает каждый из них в собственный класс
     * после чего алгоритмы можно взаимозаменять прямо во время исполнения программы.
     */
    public static void main(String[] args) {
        StrategyUsage executor = new StrategyUsage();
        executor.execute();
    }

    private void execute() {
        ListReduceProcessor strProcessor = new ListReduceProcessor<>(
                asList("Терпеть", "не", "могу", "моральных", "уродов"),
                new StringConcatenationStrategy()
        );

        System.out.println(strProcessor.reduce());

        strProcessor.setStrategy(new StringConcatenationWithUpperCaseStrategy());

        System.out.println(strProcessor.reduce());
    }
}

@AllArgsConstructor
class ListReduceProcessor<T> {
    List<T> list;
    ListReduceStrategy<T> strategy;

    public void setStrategy(ListReduceStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public T reduce() {
        return strategy.executeReduce(list);
    }
}

interface ListReduceStrategy<T> {
    T executeReduce(List<T> list);
}

class SumReduceStrategy implements ListReduceStrategy<Integer> {
    @Override
    public Integer executeReduce(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.stream().reduce(Integer::sum).orElse(0);
    }
}

class MultypleReduceStrategy implements ListReduceStrategy<Integer> {
    @Override
    public Integer executeReduce(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.stream().reduce((x, y) -> x * y).orElse(0);
    }
}

class StringConcatenationStrategy implements ListReduceStrategy<String> {
    @Override
    public String executeReduce(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream().reduce((str1, str2) -> str1 + " " + str2).orElse("");
    }
}

class StringConcatenationWithUpperCaseStrategy<T extends String> implements ListReduceStrategy<String> {
    @Override
    public String executeReduce(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream().reduce((str1, str2) -> str1.toUpperCase() + " " + str2.toUpperCase()).orElse("");
    }
}