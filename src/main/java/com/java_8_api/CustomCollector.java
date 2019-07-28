package com.java_8_api;

import com.google.common.collect.ImmutableList;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.google.common.collect.Sets.immutableEnumSet;
import static java.util.Arrays.asList;
import static java.util.stream.Collector.Characteristics.CONCURRENT;

public class CustomCollector {
    public static void main(String[] args) {
        CustomCollector executor = new CustomCollector();
        executor.execute();
    }

    private void execute() {
        ImmutableList<Long> collect = asList(1L, 2L, 3L).stream().collect(new ImmutableListCollector<>());
    }
}

class ImmutableListCollector<T>  implements Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> {
    @Override
    public Supplier<ImmutableList.Builder<T>> supplier() {
        return ImmutableList::builder;
    }

    @Override
    public BiConsumer<ImmutableList.Builder<T>, T> accumulator() {
        return ImmutableList.Builder::add;
    }

    @Override
    public BinaryOperator<ImmutableList.Builder<T>> combiner() {
        return (target, source) -> target.addAll(source.build());
    }

    @Override
    public Function<ImmutableList.Builder<T>, ImmutableList<T>> finisher() {
        return ImmutableList.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return immutableEnumSet(CONCURRENT);
    }
}