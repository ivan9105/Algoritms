package com.jmm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import static com.util.GcUtil.finalization;

public class PhantomReferenceExample {
    /**
     * Особенностей у этого типа ссылок две.
     * Первая это то, что метод get() всегда возвращает null.
     * Именно из-за этого PhantomReference имеет смысл использовать только вместе с ReferenceQueue.
     * Вторая особенность – в отличие от SoftReference и WeakReference, GC добавит phantom-ссылку в ReferenceQueue послетого как выполниться метод finalize().
     * Тоесть фактически, в отличии от SoftReference и WeakReference, объект еще есть в памяти.
     */
    public static void main(String[] args) {
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
