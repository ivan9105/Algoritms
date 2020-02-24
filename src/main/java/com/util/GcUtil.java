package com.util;

import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.min;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.awaitility.Awaitility.await;

public class GcUtil {
    private GcUtil() {
    }

    /**
     * Пытаемся вызвать finalize
     *
     * @param threshold finalization duration
     * @return gc call count
     */
    public static int finalization(@Nullable Duration threshold) {
        if (threshold == null) {
            threshold = Duration.of(10, SECONDS);
        }

        CountDownLatch latch = new CountDownLatch(1);
        ReferenceQueue<FinalizerMonitor> queue = new ReferenceQueue<>();
        PhantomReference<FinalizerMonitor> ref = new PhantomReference<>(new FinalizerMonitor(latch), queue);

        long deadline = nanoTime() + threshold.toNanos();
        boolean finalizationCalled = latch.getCount() == 0;
        int gcCallCount = 0;

        do {
            if (nanoTime() - deadline >= 0L) {
                throw new RuntimeException("Time is over");
            }

            runFinalization();
            gc();
            gcCallCount++;
            if (!finalizationCalled) {
                //блокируемся на одну секунду до тех пор пока не будет вызван count down
                try {
                    finalizationCalled = latch.await(1L, TimeUnit.SECONDS);
                } catch (Exception ignore) {
                }
            }
        } while (!finalizationCalled || !ref.isEnqueued());
        await().atMost(1L, TimeUnit.SECONDS)
                .pollInterval(10L, NANOSECONDS)
                .until(() -> queue.poll() != null);
        return gcCallCount;
    }

    public static void tryToAllocateAllAvailableMemory() {
        try {
            List<Object[]> allocations = new ArrayList<>();
            long size;
            while ((size = freeMemory()) > 0) {
                allocations.add(new Object[(int) min(size, 2147483647)]);
                out.println("Free memory:" + freeMemory());
            }
        } catch (OutOfMemoryError ignore) {
            out.println("Free memory:" + freeMemory());
        }
    }

    private static long freeMemory() {
        return getRuntime().freeMemory();
    }

    @AllArgsConstructor
    private static class FinalizerMonitor {
        private CountDownLatch latch;

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            latch.countDown();
        }
    }
}
