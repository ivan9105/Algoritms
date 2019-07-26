package com.java_8_api;

import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

public class SpliteratorUsage {
    public static void main(String[] args) {
        SpliteratorUsage executor = new SpliteratorUsage();
        executor.execute();
    }

    private static final String[] TAGS = {"Домклик", "Деньги", "Трамп", "Java8", "Java11", "Обе две"};

    private void execute() {
        Random random = new Random();
        final Long[] idCount = {0L};
        Function<Random, List<String>> generateTags = generateTagsFunction();

        List<Article> articles = Stream.generate(() ->
                Article.builder()
                        .tags(generateTags.apply(random))
                        .id(idCount[0]++)
                        .content("Test content")
                        .build()
        ).limit(1000000).collect(Collectors.toList());

        Map<String, Long> countGroupByTag = StreamSupport
                .stream(new ArticleTagsSpliterator(articles), true)
                .collect(new ArticleTagsCountCollector());


    }

    private Function<Random, List<String>> generateTagsFunction() {
        return random -> {
            int size = random.nextInt(4);
            if (size == 0) {
                size = 1;
            }

            Set<String> res = new HashSet<>();
            for (int i = 0; i < size; i++) {
                res.add(TAGS[random.nextInt(TAGS.length - 1)]);
            }
            return new ArrayList<>(res);
        };
    }
}

@Data
@Builder
class Article {
    private Long id;
    private String content;
    private List<String> tags;
}

class ArticleTagsCountCollector implements Collector<Article, ArticleTagsCountCollector.Accumulator, Map<String, Long>> {
    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }

    /**
     * логика расчета и заполнения accumulator
     */
    @Override
    public BiConsumer<Accumulator, Article> accumulator() {
        return ((accumulator, article) -> article.getTags().forEach(tag -> {
            accumulator.tagsCountMap.putIfAbsent(tag, new CountHolder());
            accumulator.tagsCountMap.get(tag).increment();
            if (!tag.equals(accumulator.currentTag)) {
                accumulator.currentTag = tag;
            }
        }));
    }

    /**
     * логика слияния результатов accumulator
     */
    @Override
    public BinaryOperator<Accumulator> combiner() {
        return (current, another) -> {
            for (String tag : current.tagsCountMap.keySet()) {
                another.tagsCountMap.putIfAbsent(tag, new CountHolder());
                another.tagsCountMap.get(tag).add(current.tagsCountMap.get(tag).count);
            }
            return another;
        };
    }

    /**
     * логика получения результата общего который ожидаем на выходе
     */
    @Override
    public Function<Accumulator, Map<String, Long>> finisher() {
        return (accumulator -> accumulator.tagsCountMap.entrySet().stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().count
                        )
                )
        );
    }

    @Override
    public Set<Characteristics> characteristics() {
        return ImmutableSet.of();
    }

    class Accumulator {
        Map<String, CountHolder> tagsCountMap = new HashMap<>();
        String currentTag;
    }

    class CountHolder {
        Long count = 0L;

        void increment() {
            count++;
        }

        void add(Long value) {
            count += value;
        }
    }
}

/**
 * Нужен исключительно для параллельной обработки
 */
class ArticleTagsSpliterator implements Spliterator<Article> {

    private List<Article> articles;
    private int currentPos;
    private int lastPos;

    public ArticleTagsSpliterator(List<Article> articles) {
        this.articles = articles;
        this.lastPos = articles.size() - 1;
    }

    public ArticleTagsSpliterator(List<Article> articles, int currentPos, int lastPos) {
        this.articles = articles;
        this.currentPos = currentPos;
        this.lastPos = lastPos;
    }

    /**
     * применяется логика коллектора
     */
    @Override
    public boolean tryAdvance(Consumer<? super Article> action) {
        if (currentPos <= lastPos) {
            action.accept(articles.get(currentPos));
            currentPos++;
            return true;
        }
        return false;
    }

    /**
     * пытаемся разбить на параллельные стримы по какому то признаку
     */
    @Override
    public Spliterator<Article> trySplit() {
        if (lastPos - currentPos < 50) {
            return null;
        }

        int splitPos = currentPos + (lastPos - currentPos) / 2;

        String firstTagBeforeSplit = articles.get(splitPos - 1).getTags().get(0);
        String firstTagAfterSplit = articles.get(splitPos).getTags().get(0);

        while (firstTagBeforeSplit.equals(firstTagAfterSplit)) {
            splitPos++;
            firstTagBeforeSplit = firstTagAfterSplit;
            firstTagAfterSplit = articles.get(splitPos).getTags().get(0);
        }

        ArticleTagsSpliterator secondSpliterator = new ArticleTagsSpliterator(articles, splitPos, lastPos);

        //обновляем позицию текущего сплитератора
        lastPos = splitPos - 1;

        return secondSpliterator;
    }

    @Override
    public long estimateSize() {
        return lastPos - currentPos;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}