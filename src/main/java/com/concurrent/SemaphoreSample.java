package com.concurrent;

import java.util.concurrent.Semaphore;

//TODO коммент как работает
public class SemaphoreSample {
    private static final int PLACES_LIMIT = 5;
    private static final boolean[] PLACES_LIMIT_STATE = new boolean[PLACES_LIMIT];
    private static final Semaphore SEMAPHORE = new Semaphore(PLACES_LIMIT, true);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 11; i++) {
            new Thread(new PlaceHolder(i)).start();
            Thread.sleep(400);
        }
    }

    public static class PlaceHolder implements Runnable {
        private int id;

        public PlaceHolder(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println(String.format("Place holder '%s' is ready", id));
            try {
                //semaphore take resource
                SEMAPHORE.acquire();

                int placeId = -1;
                synchronized (PLACES_LIMIT_STATE) {
                    for (int i = 0; i < PLACES_LIMIT; i++) {
                        if (!PLACES_LIMIT_STATE[i]) {
                            PLACES_LIMIT_STATE[i] = true;
                            placeId = i;
                            System.out.println(String.format("Place holder '%s' takes place %s", id, i));
                            break;
                        }
                    }
                }

                Thread.sleep(5000);

                synchronized (PLACES_LIMIT_STATE) {
                    PLACES_LIMIT_STATE[placeId] = false;
                }

                //semaphore release resource
                SEMAPHORE.release();
                System.out.println(String.format("Place holder '%s' releases place %s", id, placeId));
            } catch (InterruptedException ignore) {
            }
        }
    }
}
