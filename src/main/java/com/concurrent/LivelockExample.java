package com.concurrent;

public class LivelockExample {
    public static void main(String[] args) {
        Criminal criminal = new Criminal();
        Police police = new Police();

        new Thread(() -> police.giveRansom(criminal)).start();
        new Thread(() -> criminal.releaseHostage(police)).start();
    }

    public static class Criminal {
        private boolean hostageReleased = false;

        public void releaseHostage(Police police) {
            //пока полиция не пренесет деньги
            while (!police.isMoneySent()) {

                //ransom - выкуп
                //преступник будет держать заложников
                System.out.println("Criminal: waiting police to give ransom");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            //преступник дождался выкупа
            System.out.println("Criminal: released hostage");

            this.hostageReleased = true;
        }

        boolean isHostageReleased() {
            return this.hostageReleased;
        }
    }

    public static class Police {
        private boolean moneySent = false;

        public void giveRansom(Criminal criminal) {
            //пока преступник не отдаст заложников
            while (!criminal.isHostageReleased()) {

                //полиция ждет и не дает деньги
                System.out.println("Police: waiting criminal to release hostage");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            //полиция отдала деньги
            System.out.println("Police: sent money");

            this.moneySent = true;
        }

        boolean isMoneySent() {
            return this.moneySent;
        }

    }
}
