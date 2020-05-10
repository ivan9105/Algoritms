package com.concurrent;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.Comparator.comparingLong;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ConcurrentDataStructuresUsage {

    //Todo по всем тудушкам в проекте потом пройтись

    @Test
    public void copyOnWriteArrayList() {
        /**
         * был создан для обеспечения возможности безопасной перебора элементов,
         * даже когда базовый список изменяется, например другим потоком в момент перебора
         *
         * вся особенность в COWIterator который мы получаем в ответ
         * он копирует текущее состояние листа тем самым мы проходимся по неизменяемой коллекции каждый раз
         *
         * *remove не поддерживается данным iterator-ом
         */

        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>(new Integer[]{1, 2, 3, 4, 5});
        newSingleThreadExecutor().submit(() -> IntStream.range(6, 100).forEach(item -> {
            copyOnWriteArrayList.add(item);
            sleepWithoutCheckedEx(500);
            println(format("Add item '%d' to list", item));
        }));

        List<Integer> iteratedItemsList = new ArrayList<>();
        copyOnWriteArrayList.forEach(item -> {
            sleepWithoutCheckedEx(300);
            iteratedItemsList.add(item);
            println(format("Read item '%d' from copy list", item));
        });
        assertNotEquals(copyOnWriteArrayList.size(), iteratedItemsList.size());
    }

    @Test
    public void priorityBlockingQueue() {
        /**
         * Является одной из реализаций blocking queue
         * Основной фичей является то что первый элемент всегда с наивысшим приоритетом
         * приоритет определяется c помощью Comparable#compareTo либо в конструктор можно передать свой Comparator
         * не поддерживает null элементы
         *
         * add - добавляет элемент в очередь (вызывает offer)
         * offer - добавляет элемент в очередь
         * remove - удалеяет single instance of specified element
         * poll - удаляет и возвращает head элемент очереди (без ожидания)
         * poll with timeout - удаляет и возвращает head элемент очереди, ожидая при этом некоторое время чтобы элемент был доступен
         * take - удаляет и возвращает head элемент очереди (с ожиданием)
         * put - добавляет элемент в очередь (вызывает offer)
         * clear - очищает очередь
         * comparator - вернет comparator
         * contains
         * iterator
         * size
         * drainTo - удалить все элементы и закинуть их в другую коллекцию
         * drainTo with max elements size - удалить все элементы в пределах maxSize и закинуть их в другую коллекцию
         * remainingCapacity - оставшееся место, у очередь оно ограничено размерами int-a
         * toArray
         */

        List<Person> orderedPersons = new ArrayList<>();

        Person firstPerson = new Person(40);
        Person secondPerson = new Person(90);
        Person thirdPerson = new Person(4);

        BlockingQueue<Person> queue = new PriorityBlockingQueue<>(4);
        queue.offer(firstPerson);
        queue.offer(secondPerson);
        queue.offer(thirdPerson);

        queue.drainTo(orderedPersons);

        assertThat(orderedPersons).containsExactly(secondPerson, firstPerson, thirdPerson);
    }

    @Test
    public void priorityBlockingQueueTakeWithBlockingAndPriority() {
        /**
         * поток вызвавший take() будет в состоянии до тех пор пока в очереди не появиться элемент
         */

        BlockingQueue<Person> queue = new PriorityBlockingQueue<>(4);
        new Thread(() -> {
            try {
                Person person = queue.take();
                println(person.toString());
            } catch (InterruptedException ignore) {
            }
        }).start();

        println("Add element to queue");
        sleepWithoutCheckedEx(100);
        queue.offer(new Person(44));
        sleepWithoutCheckedEx(500);
    }

    @ToString
    @AllArgsConstructor
    private static class Person implements Comparable<Person> {
        private final Integer age;

        @Override
        public int compareTo(Person another) {
            return age.compareTo(another.age) * -1;
        }
    }

    @Test
    public void concurrentSkipListMap() {
        /**
         * SortedMap -> NavigableMap -> Имплементация TreeMap, ConcurrentSkipListMap и прочее
         * SortedMap:
         * Все элементы отсортиорваны в порядке возрастания ключей, для сортировки исп. Comparable
         * firstKey - возвращает ключ первого элемента отображения
         * lastKey - возвращает ключ последнего элемента отображения
         * headMap - возвращает копию sorted map до указаного ключа endKey
         * subMap - возвращает копию sorted map с указаного ключа startKey
         *
         * NavigableMap:
         * Обеспечивает возможность получения элементов отображения относительно других элементов
         * ceilingEntry - возвращает элемент с наименьшим ключом k, который больше или равен ключу obj (k >=obj) nullable
         * floorEntry - возвращает элемент с наибольшим ключом k, который меньше или равен ключу obj (k <=obj) nullable
         * higherEntry - возвращает элемент с наименьшим ключом k, который больше ключа obj (k >obj) nullable
         * lowerEntry - возвращает элемент с наибольшим ключом k, который меньше ключа obj (k <obj) nullable
         * firstEntry - возвращает первый элемент отображения
         * lastEntry - возвращает последний элемент отображения
         * pollFirstEntry - возвращает и удаляет первый элемент отображения
         * pollLastEntry - возвращает и удаляет последний элемент отображения
         * С ключами такая же логика как и с entry выше
         * ceilingKey
         * floorKey
         * lowerKey
         * higherKey
         * descendingKeySet - возвращает NavigableSet (TreeSet impl) где ключи отсортированы в обратном порядке
         * descendingMap - возвращает NavigableMap где ключи отсортированы в обратном порядке
         * navigableKeySet - возвращает NavigableSet где ключи в нормальном порядке
         * headMap - работает похожим образом как в SortedMap
         * tailMap - работает похожим образом как в SortedMap
         * subMap - mix headMap/tailMap
         * ConcurrentSkipListMap:
         */

        /**
         * Skip list:
         * вероятностная структура данных,
         * основанная на нескольких параллельных отсортированных связных списках с эффективностью,
         * сравнимой с двоичным деревом (порядка O(log n) среднее время для большинства операций).
         * https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81_%D0%BF%D1%80%D0%BE%D0%BF%D1%83%D1%81%D0%BA%D0%B0%D0%BC%D0%B8
         * суть в том что у нас не только связанный отсортированный список в основе
         * но сверху над ним еще несколько уровней с ограниченными интервалами с данными из этого списка
         * если сравнивать
         * скоростная электричка останавливается только на главных станциях
         * обычная абсолютно на всех
         */

        /**
         * основная фича - быстрая сортировка и вставка в карту, аналог TreeMap ток конкурентный
         */

        Comparator<ZonedDateTime> zonedDateTimeComparator = comparingLong(value -> value.toInstant().toEpochMilli());
        ConcurrentSkipListMap<ZonedDateTime, String> map = new ConcurrentSkipListMap<>(zonedDateTimeComparator);

        int nThreads = 3;
        ExecutorService executorService = newFixedThreadPool(nThreads);
        Runnable producer = () -> IntStream
                .rangeClosed(0, 100)
                .forEach(index -> {
                    ZonedDateTime key = ZonedDateTime.now().minusSeconds(index);
                    String value = UUID.randomUUID().toString();
                    map.put(key, value);
                });

        for (int i = 0; i < nThreads; i++) {
            executorService.execute(producer);
        }

        sleepWithoutCheckedEx(500);

        ZonedDateTime fromKey = ZonedDateTime.now().minusMinutes(1);
        //получим значение с now() - 1 minute до максимального значения в карте
        ConcurrentNavigableMap<ZonedDateTime, String> tailSubMap = map.tailMap(fromKey);
        ZonedDateTime nowMinusOneMinute = ZonedDateTime.now().minusMinutes(1);
        boolean hasOlderThanOneMinute = tailSubMap.keySet().stream()
                .anyMatch(time -> time.isBefore(nowMinusOneMinute));
        assertFalse(hasOlderThanOneMinute);

        boolean hasYoungerThanOneMinute = tailSubMap.keySet().stream()
                .anyMatch(time -> time.isAfter(nowMinusOneMinute));
        assertTrue(hasYoungerThanOneMinute);
    }

    @Test
    public void arrayBlockingQueue() {
        /**
         * Общее про интерфейс BlockingQueue
         * все реализации данного интерфейса делают блокировку потоков в двух случаях
         * при попытке получения элемента из пустой очереди
         * при попытке добавления элемента в заполненную очередь
         *
         * BlockingQueue
         * add
         * contains
         * offer
         * offer with timeout подождать определенное время пока не освободиться место (смотреть нужно имплементацию, не все поддерживают)
         * poll with timeout аналогично (без удаления)
         * put
         * remainingCapacity
         * remove
         * take (с удалением)
         *
         * ArrayBlockingQueue — очередь, реализующая классический кольцевой буфер (первый и последний элемент являются соседями)
         * а также объем очереди фиксированный
         * Данный класс поддерживает дополнительную политику справедливости параметром fair
         * в конструкторе для упорядочивания работы ожидающих потоков производителей
         *
         */

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);
        //TODO
    }

    @Test
    public void linkedBlockingQueue() {
        /**
         * LinkedBlockingQueue vs ArrayBlockingQueue:
         * LinkedBlockingQueue - может создавать без capacity с рамером границ int
         * LinkedBlockingQueue - исп Node а не items array
         * LinkedBlockingQueue - лучшая пропускная способность
         * ArrayBlockingQueue - один lock with double condition algorithm (по факту один лок для двух операций
         * которые не могут идти одновременно получается в отличие от
         * linked имплементации, поэтому и пропускная способность ниже
         * LinkedBlockingQueue - два отдельных lock один для вставки другой для удаления
         */

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    }

    @Test
    public void delayQueue() {
        /**
         * Еще один вид блокирующих очередей
         * когда потребитель хочет извлечь элемент он может его получить после некоторой задержки
         */

        ExecutorService executor = newFixedThreadPool(2);
        BlockingQueue<DelayedPerson> queue = new DelayQueue<>();
        int nItems = 3;

        Runnable producer = () -> {
            for (int i = 1; i <= nItems; i++) {
                /**
                 * start time = now() + (i * 1000) ms
                 */
                DelayedPerson delayedPerson = new DelayedPerson(i + 10, i * 1000);
                println(format("Add DelayedPerson: %s", delayedPerson));
                try {
                    queue.put(delayedPerson);
                } catch (InterruptedException ignore) {
                }
            }
        };

        Runnable consumer = () -> {
            for (int i = 0; i < nItems; i++) {
                try {
                    DelayedPerson delayedPerson = queue.take();
                    println(format("Read DelayedPerson: %s", delayedPerson.toString()));
                } catch (InterruptedException ignore) {
                }
            }
        };

        executor.submit(producer);
        executor.submit(consumer);

        sleepWithoutCheckedEx(4000);
    }

    @ToString
    private static class DelayedPerson implements Delayed {
        private final Integer age;
        private long startTimeInMs;

        public DelayedPerson(Integer age, long startTimeInMs) {
            this.age = age;
            this.startTimeInMs = currentTimeMillis() + startTimeInMs;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = startTimeInMs - currentTimeMillis();
            return unit.convert(diff, MILLISECONDS);
        }

        @Override
        public int compareTo(@Nonnull Delayed another) {
            if (!(another instanceof DelayedPerson)) {
                throw new IllegalArgumentException("Could't compare to with not DelayedPerson.class");
            }
            return this.age.compareTo(((DelayedPerson) another).age);
        }
    }

    @Test
    public void synchronousQueue() {
        /**
         * SynchronousQueue vs LinkedBlockingQueue
         * SynchronousQueue put - заблочит вызывающий thread до тех пор пока не будет вызван take другого потока
         */

        ExecutorService executor = newFixedThreadPool(3);
        BlockingQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        Random random = new Random();

        //пример с одним потоком который делает put
        Runnable producer = () -> {
            try {
                sleepWithoutCheckedEx(300);
                long id = Thread.currentThread().getId();
                int item = random.nextInt();
                println(format("Thread: %d, добавляет новый элемент в очередь: %d", id, item));
                synchronousQueue.put(item);
                println(format("Thread: %d, успешно добавил элемент в очередь", id));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                sleepWithoutCheckedEx(1300);
                long id = Thread.currentThread().getId();
                println(format("Thread: %d, пытается вычитать элемент из очереди", id));
                Integer item = synchronousQueue.take();
                println(format("Thread: %d, успешно вычитал элемент из очереди: %d", id, item));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.submit(producer);
        executor.submit(consumer);

        sleepWithoutCheckedEx(1500);

        //пример с неск потоками
        executor.submit(producer);
        //этот поток не выполнит свою работу потому что будет заблокирован
        executor.submit(producer);
        executor.submit(consumer);
        sleepWithoutCheckedEx(1500);
    }

    @Test
    public void linkedTransferQueue() {
        /**
         * LinkedTransferQueue более современная версия SynchronousQueue
         * ее можно исп как LinkedBlockingQueue
         * и выигрывает по производительности у SynchronousQueue
         */

        BlockingQueue<Integer> linkedTransferQueue = new LinkedTransferQueue<>();
    }

    private void sleepWithoutCheckedEx(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

    private static void println(String trey) {
        System.out.println(trey);
    }
}
