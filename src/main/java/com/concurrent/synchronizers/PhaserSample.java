package com.concurrent.synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class PhaserSample {
    private static final Phaser PHASER = new Phaser(1);

    public static void main(String[] args) {
        List<PlaceHolder> holders = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            if ((int) (Math.random() * 2) > 0) {
                holders.add(new PlaceHolder(i, i + 1));
            }

            if ((int) (Math.random() * 2) > 0) {
                holders.add(new PlaceHolder(i, 5));
            }
        }

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    System.out.println("Process is beginning");
                    PHASER.arrive();
                    break;
                case 6:
                    System.out.println("Process is ending");
                    PHASER.arriveAndAwaitAdvance();
                    break;
                default:
                    int currentPhase = PHASER.getPhase();
                    System.out.println(String.format("Phase '%s'", currentPhase));
                    for (PlaceHolder holder : holders) {
                        if (holder.getDeparturePhase() == currentPhase) {
                            PHASER.register();
                            holder.start();
                        }
                    }
                    PHASER.arriveAndAwaitAdvance();
            }
        }
    }

    public static class PlaceHolder extends Thread {
        private int departurePhase;
        private int destinationPhase;

        public PlaceHolder(int departurePhase, int destinationPhase) {
            this.departurePhase = departurePhase;
            this.destinationPhase = destinationPhase;
        }

        @Override
        public void run() {
            try {
                System.out.println(this + " do begin process");

                while (PHASER.getPhase() < destinationPhase) {
                    PHASER.arriveAndAwaitAdvance();
                }

                Thread.sleep(1);
                System.out.println(this + " do end process");
                PHASER.arriveAndDeregister();
            } catch (InterruptedException ignore) {
            }
        }

        public int getDeparturePhase() {
            return departurePhase;
        }

        public void setDeparturePhase(int departurePhase) {
            this.departurePhase = departurePhase;
        }

        public int getDestinationPhase() {
            return destinationPhase;
        }

        public void setDestinationPhase(int destinationPhase) {
            this.destinationPhase = destinationPhase;
        }

        @Override
        public String toString() {
            return String.format("Place holder, departure: '%s', destination: %s", departurePhase, destinationPhase);
        }
    }
}
