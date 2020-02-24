package com.jmm;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class WeakReferenceExample {
    /**
     * Если GC видит что объект ссылается на weakRef то удаляет его
     * слабая ссылка помещается в очередь ссылок
     * и ранее доступные объекты удаляются
     */
    public static void main(String[] args) {
        WeakReferenceExample runner = new WeakReferenceExample();
//        runner.sample1();
//        runner.sample2();
        runner.sample3();
    }

    public void sample3() {
        WeakHashMap<TargetKey, TargetValue> map = new WeakHashMap<>();
        TargetKey firstKey = new TargetKey("key");
        TargetValue firstValue = new TargetValue("value");
        map.put(firstKey, firstValue);

        TargetKey secondKey = new TargetKey("key2");
        TargetValue secondValue = new TargetValue("value2");
        map.put(secondKey, secondValue);

        firstKey = null;
        System.gc();

        await().atMost(10, SECONDS).until(() -> map.size() == 1);
        await().atMost(10, SECONDS).until(() -> map.containsKey(secondKey));
    }

    public void sample2() {
        WeakHashMap<TargetKey, TargetValue> map = new WeakHashMap<>();
        TargetKey key = new TargetKey("key");
        TargetValue value = new TargetValue("value");
        map.put(key, value);

        key = null;
        System.gc();

        //когда ссылка на ключ уже никем не используется запись удаляется из карты)
        await().atMost(10, SECONDS).until(map::isEmpty);
    }

    public void sample1() {
        TargetValue targetValueObj = new TargetValue();
        targetValueObj.print();

        WeakReference<TargetValue> weakRef = new WeakReference<>(targetValueObj);

        targetValueObj = null;

        targetValueObj = weakRef.get();
        targetValueObj.print();

        System.gc();

        targetValueObj = null;

        await().atMost(1000, SECONDS).until(() -> weakRef.get() == null);
    }

    public class TargetKey {
        private String id;

        public TargetKey(String id) {
            this.id = id;
        }
    }

    public class TargetValue {
        private String id;

        public TargetValue() {
        }

        public TargetValue(String id) {
            this.id = id;
        }

        public void print() {
            System.out.println("Print...");
        }
    }
}
