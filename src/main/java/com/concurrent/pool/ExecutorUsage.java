package com.concurrent.pool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Stream.iterate;

public class ExecutorUsage {
    public static void main(String[] args) {
        ExecutorUsage executor = new ExecutorUsage();
        executor.execute();
    }

    private void execute() {
        //для callable используется future get approach
        runCachedThreadPool();
    }

    private void caughtExceptionForRunnableTask() {
        //работает при вызове через execute
        UncaughtExceptionHandler handler = new UncaughtExceptionHandler();
        UncaughtExceptionHandlerThreadFactory threadFactory = new UncaughtExceptionHandlerThreadFactory(handler);
        ExecutorService executorService = newSingleThreadExecutor(threadFactory);
        executorService.execute(new ThrowableTask(1000L));
        try {
            sleep(4000L);
        } catch (InterruptedException ignore) {
        }
        executorService.shutdownNow();
    }

    private void runCachedThreadPool() {
        try {
            //не ограничен количеством, на каждую таску новый поток, если поток высвобождается то используется для новой таски
            ExecutorService executorService = newCachedThreadPool();

            sleep(1000L);
            executorService.submit(new CurrentThreadIdTask(2000L));
            executorService.submit(new CurrentThreadIdTask(5000L));

            sleep(1000L);
            executorService.submit(new CurrentThreadIdTask(3000L));

            sleep(1000L);
            executorService.submit(new CurrentThreadIdTask(4000L));

            sleep(1000L);
            executorService.submit(new CurrentThreadIdTask(1000L));

        } catch (InterruptedException ignore) {
        }
    }

    private void invokeAny() {
        ExecutorService executor = newWorkStealingPool();
        try {
            //возвращает первый вернувшийся результат
            String result = executor.invokeAny(getCallableStrList());
            System.out.println(result);
        } catch (Exception ignore) {
        }
    }

    private void invokeAll() {
        //fork join pool
        ExecutorService executor = newWorkStealingPool();

        try {
            executor.invokeAll(getCallableStrList()).stream()
                    .map(future -> {
                        try {
                            //текущий поток ждет получения самого последнего future
                            return future.get();
                        }
                        catch (Exception ignore) {
                            return "Future was not returned";
                        }
                    })
                    .forEach(System.out::println);
        } catch (InterruptedException ignore) {
        }
    }

    private void awaitTermination() {
        ExecutorService executor = newSingleThreadExecutor();
        try {
            executor.submit(new InfinityTask("Task 777"));
            executor.shutdown();
            executor.awaitTermination(5, SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Task interrupted exception");
        } finally {
            executor.shutdownNow();
            System.out.println("Force shutdown");
        }
    }

    private void runRunnablePredefinedResult() {
        ExecutorService executor = newSingleThreadExecutor();
        String predefinedResult = "result";
        Future<String> submit = executor.submit(() -> {
                    try {
                        sleep(1000L);
                    } catch (InterruptedException ignore) {
                    }
                }, predefinedResult
        );

        printFutureString(submit);
    }

    private void runCallableSubmit() {
        ExecutorService executor = newSingleThreadExecutor();
        Future<String> future = executor.submit(getCallableString());

        printFutureString(future);
    }

    private void runSingleThreadPool() {
        ExecutorService executor = newSingleThreadExecutor();
        iterate(0, n -> n + 1)
                .limit(5)
                .forEach(i -> executor.execute(new Task("Task" + i)));
    }

    private void runScheduledThreadPool() {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) newScheduledThreadPool(2);
        int initialDelay = 2;
        int delay = 2;
        executor.scheduleWithFixedDelay(new Task("Repeatable task"), initialDelay, delay, SECONDS);
    }

    private void runFixedThreadPool() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) newFixedThreadPool(2);
        iterate(0, n -> n + 1)
                .limit(10)
                .forEach(i -> executor.execute(new Task("Task" + i)));
        executor.shutdown();
    }

    private Callable<String> getCallableString() {
        return getCallableString(3000L, "Callable");
    }

    private Callable<String> getCallableString(Long timeoutInMs, String result) {
        return () -> {
            try {
                sleep(timeoutInMs);
            } catch (InterruptedException ignore) {
            }
            return result;
        };
    }

    private void printFutureString(Future<String> submit) {
        try {
            System.out.println(submit.get());
        } catch (Exception ignore) {
        }
    }

    private List<Callable<String>> getCallableStrList() {
        return asList(
                getCallableString(1200L, "result_1"),
                getCallableString(500L, "result_2"),
                getCallableString(2200L, "result_3")
        );
    }
}

@Data
@AllArgsConstructor
class Task implements Runnable {
    private String name;

    @SneakyThrows
    public void run() {
        sleep(1000L);
        System.out.println(format("Finished task '%s'", name));
    }
}

@Data
@AllArgsConstructor
class InfinityTask implements Runnable {
    private String name;

    @SneakyThrows
    public void run() {
        while (true) {
            sleep(1000L);
            System.out.println(format("Iteration infinity task '%s'", name));
        }
    }
}

@Data
@AllArgsConstructor
class CurrentThreadIdTask implements Runnable {
    private Long sleepDelayInMs;

    @SneakyThrows
    public void run() {
        sleep(sleepDelayInMs);
        System.out.println(format("Thread id=%s", currentThread().getId()));
    }
}

@Data
@AllArgsConstructor
class ThrowableTask implements Runnable {
    private Long sleepDelayInMs;

    public void run() {
        try {
            sleep(sleepDelayInMs);
        } catch (InterruptedException ignore) {
        }
        throw new RuntimeException("Runtime exception");
    }
}

@AllArgsConstructor
class UncaughtExceptionHandlerThreadFactory implements ThreadFactory {
    private static final ThreadFactory defaultFactory = defaultThreadFactory();
    private final UncaughtExceptionHandler handler;

    @Override
    public Thread newThread(@Nonnull Runnable runnable) {
        Thread thread = defaultFactory.newThread(runnable);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }
}

class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.err.println(format("Uncaught exception is detected: %s", Arrays.toString(throwable.getStackTrace())));
    }
}