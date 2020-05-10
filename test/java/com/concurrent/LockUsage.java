package com.concurrent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.*;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.lang.Thread.State.TIMED_WAITING;
import static java.lang.Thread.State.WAITING;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.locks.LockSupport.*;
import static org.junit.Assert.assertEquals;

public class LockUsage {

    @Test
    public void lockSupportBasicSample() {
        /**
         * Исп-е - базовая сихронизация
         * Дополнительно может использоваться в качестве альтернативы
         * deprecated/unsafely deadlock produced - suspend()/resume()
         *
         * park - блокирует выполнение текущего потока
         * parkNanos/parkUntil - блокирует до определенного deadline
         * unpark - разблокировка
         */

        Thread thread = new Thread(() -> {
            println("Call #park for thread");
            park();
            sleepWithoutCheckedEx(400);
            println("Thread was unblocked");
            sleepWithoutCheckedEx(1000);
        });
        thread.start();

        sleepWithoutCheckedEx(400);
        assertEquals(WAITING, thread.getState());
        unpark(thread);
        sleepWithoutCheckedEx(700);
        assertEquals(TIMED_WAITING, thread.getState());
    }

    @Test
    public void lockSupportBlockWithDeadline() {
        new Thread(() -> {
            println("Call #parkUntil for thread");
            parkUntil(1000);
            println("Thread was unblocked");
            sleepWithoutCheckedEx(1000);
        }).start();
        sleepWithoutCheckedEx(1100);
    }

    @Test
    public void lockSupportBlockWithLockSupportNotExclusiveForOneThread() {
        Object mutex = new Object();
        Thread firstThread = new Thread(() -> park(mutex));
        firstThread.start();

        Thread secondThread = new Thread(() -> park(mutex));
        secondThread.start();

        Assertions.assertThat(mutex)
                .isEqualTo(getBlocker(firstThread))
                .isEqualTo(getBlocker(secondThread));
    }

    @Test
    public void lockWithConditionBasicSample() {
        /**
         * Мы можем использовать один lock для нескольких потоков и несколько condition-ов
         * Распаралеливание в таком случае не даст никакого толка
         * если нам необхоидимо иметь неск producer-ов
         * увеличить "мощность производства" не получиться
         * операции в блоках lock/unlock будут выполняться друг за другом
         *
         * подход к синхронизации похож на wait/notify/notifyAll
         *
         * await - заставляет вызывающий поток ждать до тех пор пока не будет вызыван signal/signalAll
         * или поток не будет прерван
         * await with timeout - тоже самое, дополнительно текущий поток будет прерван если specified waiting time elapsed (истечет)
         * awaitNanos - тоже самое что и выше только timeout in nanos time
         * awaitUninterruptibly - аналог Lock#lockInterruptibly
         * awaitUntil - until deadline - будет ждать до deadline
         * signal - wake up one waiting thread
         * signalAll - пробудить все потоки которые ожидают
         */

        GoodsStore store = new GoodsStore(1000);
        List<Thread> threads = new ArrayList<>();
        threads.add(new GoodsCustomer(store, "Store #1", 100000));
        threads.add(new GoodsFactory(store, "Factory #1", 10000000));
        threads.add(new GoodsFactory(store, "Factory #2", 10000000));


        threads.forEach(Thread::start);
        sleepWithoutCheckedEx(3000);
    }

    @Test
    public void lockWithConditionAwaitWithTimeOut() {

    }

    @Test
    public void lockWithConditionAwaitNanos() {

    }

    @Test
    public void lockWithConditionAwaitUntil() {

    }

    @Test
    public void lockWithConditionSignalAll() {
        /**
         * Разница в том что мы пробудим сразу неск потоков в ожидании если будем вызывать
         * signal all
         * для примеры если у нас уже 6 потоков ждут
         * то при вызове signal all все они продолжат свою работу
         * но опя
         */

        GoodsStore store = new GoodsStore(1000, true, 4000, 100);
        List<Thread> threads = new ArrayList<>();
        threads.add(new GoodsCustomer(store, "Store #1", 100000));
        threads.add(new GoodsFactory(store, "Factory #1", 10000000));
        threads.add(new GoodsFactory(store, "Factory #7", 10000000));
        threads.add(new GoodsFactory(store, "Factory #8", 10000000));

        threads.forEach(Thread::start);
        sleepWithoutCheckedEx(3000);
    }

    @Test
    public void simpleUnfairLock() {
        /**
         * Когда вызывается lock:
         * мы пытаемся захватить ресурс текущим потоком
         * exclusiveOwnerThread - становиться текущий поток
         * nonfairTryAcquire (Если блокировка не честная)
         * если не получается если "tryAcquire" вернет false
         * причем блокировка используется через VarHandle (аналог AtomicInteger с 2 состояниями, пока так понял)
         * мы вызовем LockSupport#park через acquireQueued
         *
         * Когда вызывается unlock:
         * вызовется tryRelease
         * если текущий потом не является owner-ом вызовается IllegalMonitorStateException
         * state = 0
         * is wait queue возьмьтеся head (пока что я думаю это один из потоков который ждет)
         * вызовется LockSupport#unpark(head.thread)
         * собственно один из потоков продолжит работу
         */

        ValueHolder holder = new ValueHolder(0);
        List<Counter> counters = new ArrayList<>();
        IntStream.range(0, 3).forEach(it -> {
            Counter counter = new Counter(holder);
            counters.add(counter);
            counter.start();
        });

        counters.forEach(counter -> {
            try {
                counter.join();
            } catch (InterruptedException ignore) {
            }
        });

        assertEquals(1000, holder.getValue());
    }

    @Test
    public void tryLock() {
        /**
         * try lock работает следующим образом
         * если lock свободен то ресурс будет захвачен и вернется true
         * если нет то вернется false
         *
         * как работает
         * 0) получает блокировку если не удерживается другим поток в пределах заданного waiting time
         * 1) получает блокировку если она не удерживается другим потоком и возвращается true
         */
        ExecutorService executor = newFixedThreadPool(5);
        Lock lock = new ReentrantLock(false);

        executor.submit(() -> {
            try {
                println("try lock with waiting in 1 second");
                sleepWithoutCheckedEx(500);
                boolean locked = lock.tryLock(1000, MICROSECONDS);
                if (locked) {
                    try {
                        sleepWithoutCheckedEx(200);
                    } finally {
                        lock.unlock();
                    }
                }
                println(format("lock was acquired: %s", locked));
            } catch (InterruptedException ex) {
                println("Exception:" + ex.getLocalizedMessage());
            }
        });

        sleepWithoutCheckedEx(2000);
    }

    @Test
    public void lockInterruptibly() {
        /**
         * То же самое что и обычный lock, но если другой поток прерывается (который захватил lock),
         * ожидающий поток сгенерирует InterruptedException
         */
        ReentrantLock lock = new ReentrantLock();
        Thread thread = new Thread(() -> {
            System.out.println("Попробуем сделать блокировку");
            try {
                lock.lockInterruptibly();
                while (true) {
                    System.out.println("Что то пошло не так ...");
                }
            } catch (InterruptedException e) {
                System.out.println("Поток который вызвал блокировку был прерван");
            }
        });
        lock.lock();
        thread.start();
        thread.interrupt();

        sleepWithoutCheckedEx(1000);
    }

    @Test
    public void stampedLockUseAsReadWrite() {
        /**
         * Работает похожим образом как и read/write lock
         * поддерживает механизм оптимистичных блокировок
         * unlock исп с исп stamp полученным от предыдущей блокировки
         */
        ExecutorService executor = newFixedThreadPool(5);
        Map<String, String> sharedMap = new HashMap<>();
        StampedLock lock = new StampedLock();

        Runnable writeTask = () -> {
            long stamp = lock.writeLock();
            try {
                sleepWithoutCheckedEx(1500);
                println("Write to shared map");
                sharedMap.put("res_1", "val");
            } finally {
                lock.unlockWrite(stamp);
            }
        };

        Runnable readTask = () -> {
            long stamp = lock.readLock();
            try {
                System.out.print("Read from shared map: ");
                println(sharedMap.get("res_1"));
                sleepWithoutCheckedEx(200);
            } finally {
                lock.unlockRead(stamp);
            }
        };

        Runnable readWithoutLockTask = () -> {
            System.out.print("Read without lock: ");
            println(sharedMap.get("res_1"));
        };

        executor.submit(writeTask);
        sleepWithoutCheckedEx(400);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readWithoutLockTask);

        executor.shutdown();

        sleepWithoutCheckedEx(3000);
    }

    @Test
    public void stampLockOptimisticLock() {
        ExecutorService executor = newFixedThreadPool(5);
        StampedLock lock = new StampedLock();

        /**
         * validate возвращает true если штамп валидный т.е. никто не использует lock для записи
         * оптимистичная блокировка не заблочит поток
         * который читает
         */

        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                println("Штамп валидный?: " + lock.validate(stamp));
                sleepWithoutCheckedEx(1000);
                println("Штамп валидный?: " + lock.validate(stamp));
                sleepWithoutCheckedEx(1000);
            } finally {
                lock.unlockRead(stamp);
            }
        });

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                println("Блокировка на запись получена");
                sleepWithoutCheckedEx(2000);
            } finally {
                lock.unlockWrite(stamp);
            }
        });

        sleepWithoutCheckedEx(3000);
    }

    @Test
    public void stampLockTryToConvertToWriteLock() {
        ExecutorService executor = newFixedThreadPool(5);
        StampedLock lock = new StampedLock();

        var ref = new Object() {
            int count = 0;
        };

        executor.submit(() -> {
            long stamp = lock.readLock();
            try {
                stamp = lock.tryConvertToWriteLock(stamp);
                System.out.printf("Значение штампа: %d%n", stamp);
                if (stamp == 0L) {
                    println("не получилось сконвертировать read lock to write lock");
                    stamp = lock.writeLock();
                }
                ref.count = 1000;
                System.out.println(ref.count);
            } finally {
                lock.unlock(stamp);
            }
        });

        sleepWithoutCheckedEx(1000);
    }

    @Test
    public void readWriteLock() {
        /**
         * До тех пор пока не будет записано значение в shared map
         * в write mode lock блоке
         * ни один другой поток не сможет вычитать значение из нее если в нем исп read mode lock
         */

        ExecutorService executor = newFixedThreadPool(5);
        Map<String, String> sharedMap = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock(false);

        Runnable writeTask = () -> {
            lock.writeLock().lock();
            try {
                sleepWithoutCheckedEx(1500);
                println("Write to shared map");
                sharedMap.put("res_1", "val");
            } finally {
                lock.writeLock().unlock();
            }
        };

        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.print("Read from shared map: ");
                println(sharedMap.get("res_1"));
                sleepWithoutCheckedEx(200);
            } finally {
                lock.readLock().unlock();
            }
        };

        Runnable readWithoutLockTask = () -> {
            System.out.print("Read without lock: ");
            println(sharedMap.get("res_1"));
        };

        executor.submit(writeTask);
        sleepWithoutCheckedEx(400);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readWithoutLockTask);

        executor.shutdown();

        sleepWithoutCheckedEx(3000);
    }

    @Test
    public void simpleFairLock() {
        //locks favor granting access to the longest-waiting thread
        /**
         * Для честной блокировки lock:
         * создается sync FairSync
         * в нем в отличии от NonFair
         * есть доп проверка !hasQueuedPredecessors
         * вернет true если существует поток предшествующий текущему потоку
         * вернет false если очередь пуста или текущий поток во главе очереди
         * это может немного повлияться на !tryAcquire
         * будет вызываться AbstractQueuedSynchronizer#addWaiter/acquireQueued
         * вот тут немного сложноватый код)
         * //TODO поковыряться попозже еще
         */
        Sleeper sleeper = new Sleeper(true);

        List<Thread> threads = new ArrayList<>();

        IntStream.range(0, 100).forEach(id -> {
            FairnessLockChecker checker = new FairnessLockChecker(format("Thread_%s", id), sleeper);
            checker.start();
            sleepWithoutCheckedEx(100);
            threads.add(checker);
        });

        threads.forEach(this::joinIgnoreThrows);
    }

    private static class FairnessLockChecker extends Thread {
        private Sleeper sleeper;

        public FairnessLockChecker(String name, Sleeper sleeper) {
            setName(name);
            this.sleeper = sleeper;
        }

        @Override
        public void run() {
            sleeper.sleep();
        }
    }

    @Getter
    private static class Sleeper {
        private Lock lock;

        public Sleeper(boolean fair) {
            lock = new ReentrantLock(fair);
        }

        public void sleep() {
            try {
                Thread thread = currentThread();
                String name = thread.getName();
                println(format("Thread with name '%s' try sleep", name));
                lock.lock();
                Thread.sleep(300);
                println(format("Thread with name '%s' sleep", name));
            } catch (InterruptedException ignore) {
            } finally {
                lock.unlock();
            }
        }
    }

    @Getter
    private static class ValueHolder {
        private int value;
        private Lock lock = new ReentrantLock();

        public ValueHolder(int value) {
            this.value = value;
        }

        public void inc() {
            try {
                lock.lock();
                value++;
                System.out.printf("Thread id: %d, count: %d\n", currentThread().getId(), value);
            } finally {
                lock.unlock();
            }
        }
    }

    @Getter
    private static class Counter extends Thread {
        private static final Integer THRESHOLD = 1000;
        private ValueHolder holder;

        public Counter(ValueHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while (holder.getValue() < THRESHOLD) {
                try {
                    holder.inc();
                    sleep(1);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    /**
     * Producer task будет ждать до тех пор
     * пока capacity >= items size
     * вызвав isFullCondition.await
     * condition-ы работают только если текущий поток захватил блокировку (Lock#lock())
     * иначе вызовется IllegalMonitorStateException
     * <p>
     * если какой то поток вызывал isEmptyCondition.wait()
     * то он находиться в ожидании и когда мы добавили хоть один товар мы можем сигназировать ему о том
     * что ты можешь купить товар
     */
    private class GoodsStore {
        private final List<Object> items = new ArrayList<>();
        private final Lock lock = new ReentrantLock(false);
        private final Condition isEmpty = lock.newCondition();
        private final Condition isFull = lock.newCondition();
        private final int capacity;
        private final boolean isSignalAllMode;
        private final long addTimeout;
        private final long removeTimeout;

        private GoodsStore(int capacity) {
            this.capacity = capacity;
            this.isSignalAllMode = false;
            this.addTimeout = 100;
            this.removeTimeout = 1000;
        }

        private GoodsStore(int capacity, boolean isSignalAllMode, long addTimeout, long removeTimeout) {
            this.capacity = capacity;
            this.isSignalAllMode = isSignalAllMode;
            this.addTimeout = addTimeout;
            this.removeTimeout = removeTimeout;
        }

        void add(Object item) {
            try {
                lock.lock();
                while (items.size() >= capacity) {
                    println("Store is full: waiting while goods will be sell");
                    isFull.await();
                }
                sleep(addTimeout);
                items.add(item);
                println(format("Item was added, store's state: %d/%d", items.size(), capacity));

                signal(isEmpty);
            } catch (InterruptedException ignore) {
            } finally {
                lock.unlock();
            }
        }

        Object remove() {
            Object item = null;
            try {
                lock.lock();

                while (items.size() <= 0) {
                    println("Store is empty: waiting while goods will be produce");
                    isEmpty.await();
                }

                sleep(removeTimeout);
                item = items.remove(items.size() - 1);
                println(format("Item was remove, store's state: %d/%d", items.size(), capacity));

                signal(isFull);
            } catch (InterruptedException ignore) {
            } finally {
                lock.unlock();
            }
            return item;
        }

        public void signal(Condition condition) {
            if (isSignalAllMode) {
                condition.signalAll();
            } else {
                condition.signal();
            }
        }
    }

    @AllArgsConstructor
    private class GoodsFactory extends Thread {
        private final GoodsStore store;
        private final String id;
        private final int nItems;

        @Override
        public void run() {
            IntStream.range(0, nItems).forEach(item -> {
                println(format("Request to produce item, factory's id: %s", id));
                store.add(new Object());
            });
        }
    }

    @AllArgsConstructor
    private class GoodsCustomer extends Thread {
        private final GoodsStore store;
        private final String id;
        private final int nItems;

        @Override
        public void run() {
            IntStream.range(0, nItems).forEach(item -> {
                println(format("Request to sell item, customer's id: %s", id));
                store.remove();
            });
        }
    }

    private void sleepWithoutCheckedEx(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

    private void joinIgnoreThrows(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException ignore) {
        }
    }

    private static void println(String trey) {
        System.out.println(trey);
    }
}
