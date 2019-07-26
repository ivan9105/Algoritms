package com.java_8_api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class StreamsUsage {
    public static void main(String[] args) {
        StreamsUsage streamApi = new StreamsUsage();
        streamApi.createStreams();
        streamApi.parallelStreams();
        streamApi.flatMap();
        streamApi.terminalOperators();
    }

    private void terminalOperators() {
        //forEach
        //count
        //collector, with any Collector implementation

        BinaryOperator<Integer> binarySum = Integer::sum;
        Integer reduceSum = Stream.of(1, 2, 3, 4, 5)
                .reduce(10, binarySum);

        Comparator<Integer> intComparator = Integer::compareTo;
        Integer min = Stream.of(1, 2, 3)
                .min(intComparator)
                .get();

        Integer max = Stream.of(1, 2, 3)
                .max(intComparator)
                .get();

        IntStream intStream = getInStream(4, 65536);
        int notAlwaysFirstNumber = getInStream(4, 65536).parallel().findAny().getAsInt();
        int alwaysFirstNumber = getInStream(4, 65536).findAny().getAsInt();
        alwaysFirstNumber = getInStream(4, 65536).findFirst().getAsInt();

        IntPredicate dividedByThreePredicate = intValue -> intValue % 3 == 0;
        boolean notTrue = getInStream(4, 65536)
                .allMatch(dividedByThreePredicate);

        boolean alwaysTrue = getInStream(4, 65536)
                .anyMatch(dividedByThreePredicate);

        alwaysTrue = getInStream(4, 65536)
                .noneMatch(intValue -> intValue > -1);

        double avg = getInStream(4, 65536)
                .average()
                .getAsDouble();

        //sum
        IntSummaryStatistics summaryStatistics = getInStream(4, 65536).summaryStatistics();
        System.out.println(summaryStatistics);

        Function<Map<String, String>, Stream<Map.Entry<String, String>>> finisher = map -> map.entrySet().stream();
        Function<List<String>, List<String>> secondFinisher = Collections::unmodifiableList;
        List<String> list2 = getStringStream()
                .collect(
                        collectingAndThen(
                                toMap(
                                        identity(),
                                        s -> s + s
                                ),
                                finisher
                        )
                )
                .map(Object::toString)
                .collect(
                        collectingAndThen(
                                toList(),
                                secondFinisher
                        )
                );
        Consumer<String> strPrintlnFun = System.out::println;
        list2.forEach(strPrintlnFun);

        System.out.println(getStringStream()
                .collect(
                        joining(" -> ", "[ ", "]")
                )
        );

        //minBy alternative
        Function<String, Integer> strLengthFun = String::length;
        Optional<String> minStrOpt = getStringStream2().min(Comparator.comparing(strLengthFun));
        minStrOpt.ifPresent(strPrintlnFun);

        Optional<String> maxStrOpt = getStringStream2().max(Comparator.comparing(strLengthFun));
        maxStrOpt.ifPresent(strPrintlnFun);

        Function<String, String> strUpperCaseFun = String::toUpperCase;
        ConcurrentHashMap<Integer, List<String>> groupByRes = getStringStream3().collect(groupingByConcurrent(
                strLengthFun,
                ConcurrentHashMap::new,
                mapping(
                        strUpperCaseFun,
                        toList()
                )
        ));
        groupByRes.entrySet().forEach(System.out::println);

        Predicate<String> strLengthMoreThanEqualsTwo = str -> str.length() >= 2;
        Map<Boolean, List<String>> partitionBy = getStringStream3()
                .collect(partitioningBy(strLengthMoreThanEqualsTwo));
        partitionBy.entrySet().forEach(System.out::println);
    }

    private void flatMap() {
        Stream.of(1, 2, 3, 4, 5, 6)
                .flatMap(intValue -> {
                    switch (intValue % 3) {
                        case 0:
                            return Stream.of(intValue, intValue * intValue, intValue * intValue * 3);
                        case 1:
                            return Stream.of(intValue);
                        case 2:
                        default:
                            return Stream.empty();
                    }
                })
                .distinct()
                .sorted()
                .skip(2)
                .limit(5)
                .forEach(System.out::println);
    }

    private void parallelStreams() {
        LongStream longStream = getInStream(0, Integer.MAX_VALUE)
                .parallel()
                .mapToLong(intValue -> (long) intValue * 10);
    }

    private void createStreams() {
        Stream<String> empty = Stream.empty();
        Stream<String> fromList = new ArrayList<String>().stream();
        Stream<Map.Entry<String, Object>> fromMap = new HashMap<String, Object>().entrySet().stream();

        String[] arr = {"a", "b", "c"};
        Stream<String> fromArr = Arrays.stream(arr);

        Stream<String> fromItems = Stream.of("a", "b", "c");
    }

    private IntStream getInStream(int min, int max) {
        return IntStream.range(min, max);
    }

    private Stream<String> getStringStream() {
        return Stream.of("a", "b", "c", "d");
    }

    private Stream<String> getStringStream3() {
        return Stream.of("ab", "c", "def", "gh", "ijk", "l", "mnop");
    }

    private Stream<String> getStringStream2() {
        return Stream.of("53252", "sgss", "sg", "2352sgsg");
    }
}
