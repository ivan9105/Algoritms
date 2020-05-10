package com.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.Assert.*;

public class CompletableFutureUsage {

    @Test
    public void creation() {
        CompletableFuture<Long> completableFuture = completedFuture(1L);
        assertTrue(completableFuture.isDone());
        //getNow if result of completable future is null return 3L
        assertEquals(1L, completableFuture.getNow(3L).longValue());
    }

    @Test
    public void runAsynchronouslyUsingForkJoinPoolExecutor() {
        //use ForkJoinPool.commonPool()
        CompletableFuture<Void> completableFuture = runAsync(() -> sleep(2000L));

        assertFalse(completableFuture.isDone());
        sleep(2300L);
        assertTrue(completableFuture.isDone());
    }

    @Test
    public void supplyAsynchronouslyUsingCustomPoolExecutor() {
        ExecutorService executorService = newFixedThreadPool(2);
        LongStream.range(1, 5).forEach(it -> supplyAsync(() -> {
            sleep(1000);
            System.out.println(format("Task number: %s", it));
            return it;
        }, executorService));

        sleep(4000L);
    }

    @Test
    public void applyingFunctionOnPreviousStage() {
        supplyAsync(() -> "Then Accept")
                .thenApply(it -> {
                    String res = it.toUpperCase();
                    System.out.println(res);
                    return res;
                })
                .thenApply(it -> {
                    String res = new StringBuilder(it.toLowerCase()).reverse().toString();
                    System.out.println(res);
                    return res;
                });
    }

    @Test
    public void thenApplyAsync() {
        //thenApplyAsync use ForkJoinPool
        CompletableFuture<String> completableFuture = completedFuture("message").thenApplyAsync(str -> {
            sleep(1000);
            return str.toUpperCase();
        });
        try {
            completableFuture.get();
        } catch (InterruptedException | ExecutionException ignored) {
        }
    }

    @Test
    public void thenApplyAsyncWithCustomExecutor() {
        completedFuture("message").thenApplyAsync(str -> {
            sleep(400);
            return str;
        }, newSingleThreadExecutor());
    }

    @Test
    public void thenApplyAsyncWithExecutionException() {
        CompletableFuture<Object> completableFuture = completedFuture("message").thenApplyAsync(str -> {
            sleep(2000);
            throw new RuntimeException("It's error");
        });

        try {
            completableFuture.get();
        } catch (InterruptedException ignored) {
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof RuntimeException);
            assertEquals(cause.getLocalizedMessage(), "It's error");
        }
    }

    @Test
    public void thanApplyAsyncJoin() {
        CompletableFuture<Void> completableFuture = runAsync(() -> {
            sleep(2000);
            System.out.println("Test");
        });

        completableFuture.join();
    }

    @Test
    public void genNow() {
        CompletableFuture<String> completableFuture = supplyAsync(() -> {
            sleep(2000);
            return "message";
        });

        assertEquals("test", completableFuture.getNow("test"));
    }

    @Test
    public void completeExceptionally() {
        CompletableFuture<String> completableFuture = completedFuture("message").thenApplyAsync(
                String::toUpperCase,
                delayedExecutor(2, TimeUnit.SECONDS)
        );

        CompletableFuture<String> exceptionHandler = completableFuture.handle(
                (str, ex) -> (ex != null) ? "handle exception" : ""
        );

        //finish task with exception
        completableFuture.completeExceptionally(new RuntimeException("It's error"));
        assertTrue(completableFuture.isCompletedExceptionally());

        try {
            completableFuture.join();
        } catch (CompletionException ex) { // just for testing
            assertEquals("It's error", ex.getCause().getMessage());
        }

        assertEquals("handle exception", exceptionHandler.join());
    }

    @Test
    public void cancel() {
        CompletableFuture<String> completableFuture = completedFuture("message").thenApplyAsync(str -> {
            sleep(2000);
            return "message";
        });

        assertTrue(completableFuture.cancel(true));
        assertTrue(completableFuture.isCancelled());

        boolean hasException = false;
        try {
            completableFuture.join();
        } catch (CancellationException ce) {
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    public void applyToEither() {
        CompletableFuture<String> completableFuture = completedFuture("Message")
                .thenApplyAsync(s -> sleepAndUpperCase(s, 2000));

        /**
         * По факту applyToEither принимает альтернативный completedFuture
         * и secondCompletableFuture и примениться тот completable future который завершиться первым
         * например если completableFuture проставить timeout 5000 ms, выполниться second completable future с
         * lower case
         */
        Function<String, String> function = str -> format("%s applyToEither", str);
        CompletableFuture<String> secondCompletableFuture = completableFuture.applyToEither(
                completedFuture("Second message").thenApplyAsync(str -> sleepAndLowerCase(str, 3350)),
                function
        );

        assertEquals("MESSAGE applyToEither", secondCompletableFuture.join());
    }

    @Test
    public void applyToEitherWithConsumer() {
        StringBuilder sb = new StringBuilder();

        Consumer<String> consumer = str -> sb.append(str).append("acceptEither");
        CompletableFuture<Void> completableFuture = completedFuture("Message")
                .thenApplyAsync(str -> sb.append(sleepAndUpperCase(str, 5000)).toString())
                .acceptEither(completedFuture("SecondMessage").thenApplyAsync(
                        str -> sb.append(sleepAndLowerCase(str, 1000)).toString()), consumer
                );

        completableFuture.join();

        assertEquals("secondmessagesecondmessageacceptEither", sb.toString());
    }

    @Test
    public void runAfterBoth() {
        StringBuilder sb = new StringBuilder();

        Runnable runnable = () -> sb.append(" runAfterBoth");

        /**
         * Подождет оба completedFuture причем в той последовательности в которой они были вызваны
         * и по окончании запустит runnable
         */
        completedFuture("Message").thenApply(str -> sb.append(sleepAndLowerCase(str, 2000))).runAfterBoth(
                completedFuture("Second message").thenApply(str -> sb.append(sleepAndLowerCase(str, 500))),
                runnable
        );
        assertEquals("messagesecond message runAfterBoth", sb.toString());
    }

    @Test
    public void acceptResultBothInBiConsumer() {
        StringBuilder sb = new StringBuilder();
        completedFuture("1").thenAcceptBoth(
                supplyAsync(() -> sleepAndLowerCase("2", 500)),
                (first, another) -> sb.append(first).append("+").append(another)
        ).join();

        assertEquals("1+2", sb.toString());
    }

    @Test
    public void combine() throws ExecutionException, InterruptedException {
        String result = completedFuture("1").thenApply(str -> sleepAndReturn(str, 500))
                .thenCombine(completedFuture("2").thenApply(str -> sleepAndReturn(str, 1000)),
                        (str1, str2) -> format("%s+%s", str1, str2)).get();
        assertEquals("1+2", result);
    }

    @Test
    public void combineAsync() throws ExecutionException, InterruptedException {
        String result = completedFuture("1").thenApplyAsync(str -> sleepAndReturn(str, 500))
                .thenCombine(completedFuture("2").thenApplyAsync(str -> sleepAndReturn(str, 1000)),
                        (str1, str2) -> format("%s+%s", str1, str2)).get();
        assertEquals("1+2", result);
    }

    @Test
    public void compose() {
        /**
         * compose будет ожидать выполнения предыдущей стадии прежде чем выполнить следующую
         */
        String result = completedFuture("1").thenApply(str -> {
            sleepAndPrint(str);
            return str;
        }).thenCompose(firstResult -> completedFuture("2")
                .thenApply(str -> sleepAndReturn(format("%s+%s", firstResult, str), 1500)))
                .thenApply(secondResult -> sleepAndReturn(secondResult, 500)).join();

        assertEquals("1+2", result);
    }

    @Test
    public void creationWhenCompleteAnyOfSeveral() {
        StringBuilder sb = new StringBuilder();

        Stream<CompletableFuture<String>> completableFutures = newArrayList("1", "2", "3").stream()
                .map(str -> completedFuture(str)
                        .thenApply(s -> sleepAndUpperCase(s, 300)));

        anyOf(completableFutures.toArray(CompletableFuture[]::new))
                .whenComplete((str, th) -> {
                    if (th == null) {
                        sb.append(str);
                    }
                });

        assertEquals("1", sb.toString());
    }

    @Test
    public void creationWhenCompleteAllFromSeveral() {
        StringBuilder sb = new StringBuilder();

        List<CompletableFuture<String>> completableFutures = newArrayList("1", "2", "3").stream()
                .map(str -> completedFuture(str)
                        .thenApply(s -> sleepAndUpperCase(s, 300))).collect(Collectors.toList());

        allOf(completableFutures.toArray(CompletableFuture[]::new))
                .whenComplete((str, th) -> {
                            completableFutures.forEach(completableFuture -> assertTrue(completableFuture.isDone()));
                            sb.append("done");
                        }
                ).join();

        assertEquals("done", sb.toString());
    }

    @Test
    public void creationWhenCompleteAllFromSeveralAsync() {
        StringBuilder sb = new StringBuilder();

        List<CompletableFuture<String>> completableFutures = newArrayList("1", "2", "3").stream()
                .map(str -> completedFuture(str)
                        .thenApplyAsync(s -> sleepAndUpperCase(s, 300))).collect(Collectors.toList());

        allOf(completableFutures.toArray(CompletableFuture[]::new))
                .whenComplete((str, th) -> {
                            completableFutures.forEach(completableFuture -> assertTrue(completableFuture.isDone()));
                            sb.append("done");
                        }
                ).join();

        assertEquals("done", sb.toString());
    }

    @Test
    public void example() {
        List<CompletableFuture<RequestDto>> dtoFutures = getRequests().join().stream()
                .map(request -> supplyAsync(() -> {
                    System.out.println(format("Convert request with id '%d' to dto", request.id));
                    BigDecimal price = calculatePrice(request.regionId).join();
                    System.out.println(format("Request's price with id '%d' = '%s'", request.id, price));
                    return RequestDto.builder()
                            .id(request.id)
                            .createdDate(request.createdDate)
                            .number(request.number)
                            .price(price)
                            .regionId(request.regionId)
                            .build();
                })).collect(Collectors.toList());

        allOf(dtoFutures.toArray(CompletableFuture[]::new))
                .whenComplete((str, th) -> System.out.println("Done")).join();
    }

    private CompletableFuture<BigDecimal> calculatePrice(String regionId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(5000);
            switch (regionId) {
                case "77":
                    return new BigDecimal("100.00");
                case "36":
                    return new BigDecimal("55.50");
                case "25":
                    return new BigDecimal("23.00");
                default:
                    return new BigDecimal("40.00");
            }
        }).exceptionally(ex -> new BigDecimal("0.00"));
    }

    private CompletableFuture<List<Request>> getRequests() {
        return supplyAsync(() -> newArrayList(
                Request.builder()
                        .id(1L)
                        .number("775234")
                        .regionId("77")
                        .createdDate(LocalDate.of(2017, 1, 1))
                        .build(),
                Request.builder()
                        .id(2L)
                        .number("775235")
                        .regionId("36")
                        .createdDate(LocalDate.of(2017, 1, 1))
                        .build(),
                Request.builder()
                        .id(3L)
                        .number("775236")
                        .regionId("25")
                        .createdDate(LocalDate.of(2018, 1, 1))
                        .build()));
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    private static class Request {
        private Long id;
        private String number;
        private String regionId;
        private LocalDate createdDate;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    private static class RequestDto {
        private Long id;
        private String number;
        private String regionId;
        private LocalDate createdDate;
        private BigDecimal price;
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ignore) {
        }
    }

    private String sleepAndUpperCase(String str, long mills) {
        sleep(mills);
        return str.toUpperCase();
    }

    private String sleepAndLowerCase(String str, long mills) {
        sleep(mills);
        return str.toLowerCase();
    }

    private <T> T sleepAndReturn(T value, long mills) {
        sleep(mills);
        return value;
    }

    private void sleepAndPrint(String str) {
        sleepAndPrint(str, 1500);
    }

    private void sleepAndPrint(String str, long mills) {
        sleep(mills);
        System.out.println(str);
    }
}
