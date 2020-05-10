package com.jmm;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

import static com.util.GcUtil.finalization;
import static java.lang.String.format;

public class PhantomReferenceExample {
    /**
     * Особенностей у этого типа ссылок две.
     * Первая это то, что метод get() всегда возвращает null.
     * Именно из-за этого PhantomReference имеет смысл использовать только вместе с ReferenceQueue.
     * Вторая особенность – в отличие от SoftReference и WeakReference, GC добавит phantom-ссылку в ReferenceQueue послетого как выполниться метод finalize().
     * Тоесть фактически, в отличии от SoftReference и WeakReference, объект еще есть в памяти.
     */
    public static void main(String[] args) throws InterruptedException {
//        example1();

        /**
         * Это может быть необходимо, когда объект что-то делает за границами Java-машины,
         * например вызывает низкоуровневые функции ОС или пишет свое состояние в файл или еще что-нибудь очень важное.
         */
        //специальная очередь для призрачных объектов
        ReferenceQueue<PhantomObject> queue = new ReferenceQueue<>();

        List<PhantomReference<PhantomObject>> list = new ArrayList<PhantomReference<PhantomObject>>();


        /**
         * При уничтожении объекта, удерживаемого призрачной ссылкой, он уничтожается, но не удаляется из памяти
         */

        for (int i = 0; i < 10; i++) {
            PhantomObject phantomObject = new PhantomObject(i);
            list.add(new PhantomReference<>(phantomObject, queue));
        }

        //попробуем удалить их
        finalization(null);

        Thread.sleep(3000L);
        Reference<? extends PhantomObject> referenceFromQueue;
        while ((referenceFromQueue = queue.poll()) != null) {
            //тут всегда будет null
            System.out.println(referenceFromQueue.get());
            //очищаем ссылку удаляем из призрачной очереди

            //смысл в том что мы может сделать наследника PhantomReference и вызывать его метод при удалении объекта
            //например мы может сделать daemon thread и в бесконечном цикле ожидать удаления объекта из памяти
            referenceFromQueue.clear();
        }
    }

    private static class PhantomObject {
        private Integer value;

        public PhantomObject(Integer value) {
            this.value = value;
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println(format("Finalize: %s object", value));
        }

        @Override
        public String toString() {
            return "PhantomObject{" +
                    "value=" + value +
                    '}';
        }
    }

    private static void example1() {
        /**
         * Этот тип ссылок и ReferenceQueue позволяет узнать когда объект уже не доступен и на него нет уже ссылок
         * в отличие от finalize мы можем сами контролировать очистку ресурсов используя эти ссылки
         *
         */

        FinalizeObject target = new FinalizeObject();
        ReferenceQueue<FinalizeObject> queue = new ReferenceQueue<>();
        PhantomReference<FinalizeObject> ref = new PhantomReference<>(target, queue);

        target = null;

        int gcPass = finalization(null);
        System.out.println("Ref is enqueued:" + ref.isEnqueued());
        System.out.println("Queue poll return ref: " + (queue.poll().equals(ref)));
    }

    private static class FinalizeObject {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("Finalize");
        }
    }
}
