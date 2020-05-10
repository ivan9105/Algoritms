package com.java_8_api;

import com.google.common.collect.ImmutableList;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.LongStream;

import static com.google.common.collect.Sets.immutableEnumSet;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collector.Characteristics.CONCURRENT;

public class CustomCollector {
    public static void main(String[] args) {
        CustomCollector executor = new CustomCollector();
        executor.execute();
    }

    private void execute() {
        System.out.println("Обычный stream");
        asList(1L, 2L, 3L).stream().collect(new ImmutableListCollector<>());

        System.out.println("Параллельный stream");
        LongStream.range(1, 1000).boxed().parallel().collect(new ImmutableListCollector<>());
    }
}

/**
 * supplier - Supplier новый контейнер результатов
 * accumulator - BiConsumer функция которая по факту добавляет элементы в контейнер результатов
 * combiner - BinaryOperator merge данных из неск контейнеров результатов (для параллельных стримов точно актуально)
 * finisher - Function приводит к окончательному виду
 * characteristics - CONCURRENT говорит о том что это можно использовать например для исп-я в параллельных стримах
 * @param <T>
 */
class ImmutableListCollector<T>  implements Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> {
    @Override
    public Supplier<ImmutableList.Builder<T>> supplier() {
        Supplier<ImmutableList.Builder<T>> res = ImmutableList::builder;
        System.out.println("Создаем supplier функцию которая возвращает builder");
        return res;
    }

    @Override
    public BiConsumer<ImmutableList.Builder<T>, T> accumulator() {
        return (builder, value) -> {
            System.out.println(format("Добавляем значение %s в builder %s", value, builder));
            builder.add(value);
        };
    }

    @Override
    public BinaryOperator<ImmutableList.Builder<T>> combiner() {
        return (target, source) -> {
            System.out.println(format("Делаем merge результатов из одного builder-a %s в другой %s", target, source));
            return target.addAll(source.build());
        };
    }

    @Override
    public Function<ImmutableList.Builder<T>, ImmutableList<T>> finisher() {
        System.out.println("Завершаем сборку результата");
        return ImmutableList.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return immutableEnumSet(CONCURRENT);
    }
}